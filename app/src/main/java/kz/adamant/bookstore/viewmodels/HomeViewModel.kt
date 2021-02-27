package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.adamant.bookstore.states.BooksViewState
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.models.Book
import kz.adamant.domain.usecases.GetAllBooksUseCase

class HomeViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase
): BaseViewModel() {

    private var _allBooks = MutableLiveData<BooksViewState>()

    init {
        _allBooks.value = BooksViewState.Loading
        launchCoroutine {
            getAllBooksUseCase().collect {
                _allBooks.value = BooksViewState.Loaded(it)
            }
        }
    }

    val allBooks get() = _allBooks
}

class HomeViewModelFactory(
        private val getAllBooksUseCase: GetAllBooksUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getAllBooksUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}