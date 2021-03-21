package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kz.adamant.bookstore.mappers.toDvo
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.bookstore.utils.ThrottledLiveData
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksNewlyAddedUseCase
import kz.adamant.domain.usecases.GetAllGenresUseCase
import kz.adamant.domain.usecases.GetBookRentingMarkUseCase
import kz.adamant.domain.usecases.GetReadingBooksUseCase
import kz.adamant.domain.utils.RENTED_BY_CURRENT_USER
import kz.adamant.domain.utils.filterThenMap

class HomeViewModel(
    private val getReadingBooksUseCase: GetReadingBooksUseCase,
    private val getAllBooksNewlyAddedUseCaseUseCase: GetAllBooksNewlyAddedUseCase,
    private val getBookRentingMarkUseCase: GetBookRentingMarkUseCase,
) : BaseViewModel() {
    private var _allReadingBooks: LiveData<Resource<List<ReadingBook>>> = MutableLiveData()
    private var _userReadingBooks: LiveData<Resource<List<BookDvo?>>> = MutableLiveData()
    private var _newlyAddedBooks: LiveData<Resource<List<BookDvo>>> = MutableLiveData()
    private val _forceRefresh = MutableLiveData<Boolean>().apply { value = true }

    init {
        _allReadingBooks = _forceRefresh.switchMap {
            launchOnViewModelScope {
                getReadingBooksUseCase.invoke(shouldFetch || it)
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
                getAllBooksNewlyAddedUseCaseUseCase.invoke(shouldFetch || it)
                    .onCompletion { determineFetching() }
                    .flatMapConcat {
                        flowOf(it.toDvo { items -> items!!.map { book -> book.toDvo(getBookRentingMarkUseCase(book.id, "123")) } })
                    }
                    .flowOn(Dispatchers.IO)
                    .asLiveData()
            }
        }
    }

    val userReadingBooks by lazy { ThrottledLiveData(_userReadingBooks, 500L) }
    val newlyAddedBooks by lazy { ThrottledLiveData(_newlyAddedBooks, 500L) }

    @Volatile
    private var shouldFetch = true

    fun setForceRefresh() {
        _forceRefresh.value = true
    }

    @Synchronized private fun determineFetching() {
        shouldFetch = false || _userReadingBooks.value?.data.isNullOrEmpty() || _userReadingBooks.value?.data.isNullOrEmpty()
    }
}