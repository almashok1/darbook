package kz.adamant.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.RentInfo
import kz.adamant.domain.models.Resource
import kz.adamant.domain.usecases.GetGenreUseCase
import kz.adamant.domain.usecases.RentBookUseCase

class BookDetailViewModel(
    private val getGenreUseCase: GetGenreUseCase,
    private val rentBookUseCase: RentBookUseCase
): BaseViewModel() {

    var genre: LiveData<Resource<Genre?>> = MutableLiveData()
    var readingBookResponse = MutableLiveData<ReadingBook?>()

    fun getGenreById(genreId: Int) {
        genre = launchOnViewModelScope {
            getGenreUseCase(genreId)
                .asLiveData()
        }
    }

    fun rentBook(bookId: Int, userId: String) {
        launchCoroutine {
            val readingBook = rentBookUseCase.invoke(RentInfo(
                bookId = bookId,
                userId = userId,
                userContact = "Contact Test",
                userName = "User Name",
                startDate = "2021-03-01T03:58:36.787Z"
            ))
            withContext(Dispatchers.Main) {
                readingBookResponse.value = readingBook
            }
        }
    }
}