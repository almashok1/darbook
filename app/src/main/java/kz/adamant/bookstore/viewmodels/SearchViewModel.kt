package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.*
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksUseCase
import kz.adamant.domain.usecases.GetAllGenresUseCase

class SearchViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase
): BaseViewModel() {
    private val _query = MutableLiveData<String?>().apply { value = null }
    private var _allBooks: LiveData<Resource<List<Book>>> = MutableLiveData()
    private var _genres: LiveData<Resource<List<Genre>>> = MutableLiveData()

    private val _selectedGenres = MutableLiveData<List<Genre>>()

    init {
        _allBooks = Transformations.switchMap(DoubleTrigger(_query, _selectedGenres)) {
            launchOnViewModelScope {
                getAllBooksUseCase(getSearchPattern(it.first), it.second).asLiveData()
            }
        }
        _genres = launchOnViewModelScope {
            getAllGenresUseCase().asLiveData()
        }
    }

    val allBooks get() = _allBooks
    val genres get() = _genres
    val query get() = _query

    private fun getSearchPattern(query: String?) = if (query != null) "%$query%" else null

    fun setSelectedGenres(genres: List<Genre>) {
        _selectedGenres.value = genres
    }
}

class DoubleTrigger<A, B>(a: LiveData<A>, b: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
    init {
        addSource(a) { value = it to b.value }
        addSource(b) { value = a.value to it }
    }
}

class SearchViewModelFactory(
        private val getAllBooksUseCase: GetAllBooksUseCase,
        private val getAllGenresUseCase: GetAllGenresUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(getAllBooksUseCase, getAllGenresUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}