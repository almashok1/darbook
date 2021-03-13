package kz.adamant.bookstore.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kz.adamant.bookstore.ui.home.AllBooksFragment
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksNewlyAddedUseCase
import kz.adamant.domain.usecases.GetAllReadingBooksUseCase

class AllBooksViewModel(
    booksType: Int,
    private val getAllReadingBooksUseCase: GetAllReadingBooksUseCase,
    private val getAllBooksNewlyAddedUseCaseUseCase: GetAllBooksNewlyAddedUseCase
): BaseViewModel() {
    private var _readingBooks: LiveData<Resource<List<ReadingBook>>> = MutableLiveData()
    private var _newlyAdded: LiveData<Resource<List<Book>>> = MutableLiveData()

    init {
        Log.d("TAG", ": ${booksType}")
        if (booksType == AllBooksFragment.NOW_READING) {
            _readingBooks = launchOnViewModelScope {
                getAllReadingBooksUseCase.invoke(false, null)
                    .asLiveData()
            }
        } else if (booksType == AllBooksFragment.NEWLY_ADDED) {
            _newlyAdded = launchOnViewModelScope {
                getAllBooksNewlyAddedUseCaseUseCase.invoke(false, null)
                    .asLiveData()
            }
        }
    }

    val readingBooks get() = _readingBooks
    val newlyAdded get() = _newlyAdded
}