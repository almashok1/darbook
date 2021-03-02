package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.collect
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetAllBooksUseCase

class HomeViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase
): BaseViewModel() {

    private var _allBooks = MutableLiveData<Resource<List<Book>>>()

    init {
        launchCoroutine {
            getAllBooksUseCase().collect {
                _allBooks.value = it
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