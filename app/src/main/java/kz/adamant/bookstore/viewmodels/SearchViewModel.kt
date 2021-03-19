package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kz.adamant.bookstore.mappers.toDvo
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksUseCase
import kz.adamant.domain.usecases.GetAllGenresUseCase
import kz.adamant.domain.usecases.GetBookRentingMarkUseCase

class SearchViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val getBookRentingMarkUseCase: GetBookRentingMarkUseCase
): BaseViewModel() {
    private var _allBooks: LiveData<Resource<List<BookDvo>>> = MutableLiveData()
    private var _genres: LiveData<Resource<List<Genre>>> = MutableLiveData()

    private val _query = MutableLiveData<String?>().apply { value = null }
    private val _selectedGenres = MutableLiveData<List<Int>?>()
    private val _forceRefresh = MutableLiveData<Boolean>()

    init {
        _genres = launchOnViewModelScope {
                getAllGenresUseCase(shouldFetchGenres)
                    .onCompletion { determineFetchGenres() }
                    .asLiveData()
            }
        _allBooks = Transformations.switchMap(TripleTrigger(_query, _selectedGenres, _forceRefresh)) {
            launchOnViewModelScope {
                getAllBooksUseCase(
                    getSearchPattern(it.first),
                    it.second,
                    shouldFetchBooks || it.third ?: false
                )
                    .onCompletion { determineFetchBooks() }
                    .flatMapConcat {
                        flowOf(it.toDvo { items -> items!!.map { book -> book.toDvo(getBookRentingMarkUseCase(book.id, "123")) } })
                    }
                    .flowOn(Dispatchers.IO)
                    .asLiveData()
            }
        }
    }

    val allBooks get() = _allBooks
    val genres get() = _genres
    val query get() = _query

    val selectedGenresId = hashSetOf<Int>()

    @Volatile
    private var shouldFetchGenres = true
    @Volatile
    private var shouldFetchBooks = true


    private fun getSearchPattern(query: String?) = if (query != null) "%$query%" else null

    fun setForceRefresh() {
        _forceRefresh.value = true
    }

    fun postSelectedGenresId(genres: HashSet<Int>) {
        _selectedGenres.value = genres.toList()
    }

    @Synchronized private fun determineFetchBooks() {
        shouldFetchBooks = false || allBooks.value?.data.isNullOrEmpty()
    }

    @Synchronized private fun determineFetchGenres() {
        shouldFetchGenres = false || genres.value?.data.isNullOrEmpty()
    }
}

class TripleTrigger<A, B, C>(first: LiveData<A?>, second: LiveData<B?>, third: LiveData<C?>) : MediatorLiveData<Triple<A?, B?, C?>>() {
    init {
        addSource(first) { value = Triple(it, second.value, third.value) }
        addSource(second) { value = Triple(first.value, it, third.value) }
        addSource(third) { value = Triple(first.value, second.value, it) }
    }
}