package kz.adamant.bookstore.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.adamant.bookstore.data.repository.BooksRepository
import kz.adamant.bookstore.models.Book

class HomeViewModel(
    private val repository: BooksRepository
): ViewModel() {

    private lateinit var _allBooks: LiveData<List<Book>>

    init {
        viewModelScope.launch {
            _allBooks = repository
                .getAllBooks()
                .asLiveData(viewModelScope.coroutineContext + Dispatchers.Default)
        }
    }

    val allBooks get() = _allBooks
}

class HomeViewModelFactory(
    private val repository: BooksRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}