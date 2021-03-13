package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.flow.onCompletion
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksNewlyAddedUseCase
import kz.adamant.domain.usecases.GetAllReadingBooksUseCase

class HomeViewModel(
    private val getAllReadingBooksUseCase: GetAllReadingBooksUseCase,
    private val getAllBooksNewlyAddedUseCaseUseCase: GetAllBooksNewlyAddedUseCase
) : BaseViewModel() {
    private var _allReadingBooks: LiveData<Resource<List<ReadingBook>>> = MutableLiveData()
    private var _topBooksNewlyAdded: LiveData<Resource<List<Book>>> = MutableLiveData()
    private val _forceRefresh = MutableLiveData<Boolean>().apply { value = true }

    init {
        _allReadingBooks = _forceRefresh.switchMap {
            launchOnViewModelScope {
                getAllReadingBooksUseCase.invoke(shouldFetchReadingBooks || it)
                    .onCompletion { determineFetchReadingBooks() }
                    .asLiveData()
            }
        }
        _topBooksNewlyAdded = _forceRefresh.switchMap {
            launchOnViewModelScope {
                getAllBooksNewlyAddedUseCaseUseCase.invoke(shouldFetchReadingBooks || it)
                    .onCompletion { determineFetchReadingBooks() }
                    .asLiveData()
            }
        }
    }


    val allReadingBooks get() = _allReadingBooks
    val topBooksNewlyAdded get() = _topBooksNewlyAdded


    @Volatile
    private var shouldFetchReadingBooks = true
    @Volatile
    private var shouldFetchBooksNewlyAdded = true

    fun setForceRefresh() {
        _forceRefresh.value = true
    }

    @Synchronized private fun determineFetchReadingBooks() {
        shouldFetchReadingBooks = false || _allReadingBooks.value?.data.isNullOrEmpty()
    }

    @Synchronized private fun determineFetchBooksNewlyAdded() {
        shouldFetchBooksNewlyAdded = false || _topBooksNewlyAdded.value?.data.isNullOrEmpty()
    }
}