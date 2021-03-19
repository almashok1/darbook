package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kz.adamant.bookstore.mappers.toDvo
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksNewlyAddedUseCase
import kz.adamant.domain.usecases.GetBookRentingMarkUseCase
import kz.adamant.domain.usecases.GetReadingBooksUseCase
import kz.adamant.domain.utils.RENTED_BY_CURRENT_USER
import kz.adamant.domain.utils.filterThenMap

class HomeViewModel(
    private val getReadingBooksUseCase: GetReadingBooksUseCase,
    private val getAllBooksNewlyAddedUseCaseUseCase: GetAllBooksNewlyAddedUseCase,
    private val getBookRentingMarkUseCase: GetBookRentingMarkUseCase
) : BaseViewModel() {
    private var _allReadingBooks: LiveData<Resource<List<ReadingBook>>> = MutableLiveData()
    private var _userReadingBooks: LiveData<Resource<List<BookDvo?>>> = MutableLiveData()
    private var _newlyAddedBooks: LiveData<Resource<List<BookDvo>>> = MutableLiveData()
    private val _forceRefresh = MutableLiveData<Boolean>().apply { value = true }

    init {
        _allReadingBooks = _forceRefresh.switchMap {
            launchOnViewModelScope {
                getReadingBooksUseCase.invoke(shouldFetchReadingBooks || it)
                    .onCompletion { determineFetchReadingBooks() }
                    .asLiveData()
            }
        }
        _userReadingBooks = _allReadingBooks.switchMap {
            launchOnViewModelScope {
                liveData {
                    emit(
                        it.toDvo {
                            items -> items?.filterThenMap(
                                predicate = { reading -> reading.userId == "123" },
                                transform = { reading -> reading.book?.toDvo(RENTED_BY_CURRENT_USER) }
                            )
                        }
                    )
                }
            }
        }
        _newlyAddedBooks = _forceRefresh.switchMap {
            launchOnViewModelScope {
                getAllBooksNewlyAddedUseCaseUseCase.invoke(shouldFetchReadingBooks || it)
                    .onCompletion { determineFetchReadingBooks() }
                    .flatMapConcat {
                        flowOf(it.toDvo { items -> items!!.map { book -> book.toDvo(getBookRentingMarkUseCase(book.id, "123")) } })
                    }
                    .flowOn(Dispatchers.IO)
                    .asLiveData()
            }
        }
    }

    val userReadingBooks get() = _userReadingBooks
    val newlyAddedBooks get() = _newlyAddedBooks

    @Volatile
    private var shouldFetchReadingBooks = true
    @Volatile
    private var shouldFetchBooksNewlyAdded = true

    fun setForceRefresh() {
        _forceRefresh.value = true
    }

    @Synchronized private fun determineFetchReadingBooks() {
        shouldFetchReadingBooks = false || _userReadingBooks.value?.data.isNullOrEmpty()
    }

    @Synchronized private fun determineFetchBooksNewlyAdded() {
        shouldFetchBooksNewlyAdded = false || _newlyAddedBooks.value?.data.isNullOrEmpty()
    }
}