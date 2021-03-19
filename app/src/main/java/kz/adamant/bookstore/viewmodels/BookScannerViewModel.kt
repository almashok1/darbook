package kz.adamant.bookstore.viewmodels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kz.adamant.bookstore.mappers.toDvo
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.bookstore.utils.SingleLiveEvent
import kz.adamant.domain.usecases.GetBookRentingMarkUseCase
import kz.adamant.domain.usecases.GetSingleBookUseCase

class BookScannerViewModel(
    private val getSingleBookUseCase: GetSingleBookUseCase,
    private val getBookRentingMarkUseCase: GetBookRentingMarkUseCase
): BaseViewModel() {

    var scannedBook = SingleLiveEvent<List<BookDvo>>()

    fun findBookByIsbn(isbn: String) {
        launchCoroutine {
            val book = getSingleBookUseCase(isbn)
            book.flatMapConcat {
                flowOf(it.map { book -> book.toDvo(getBookRentingMarkUseCase(book.id, "123"))})
            }
                .flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main) {
                        scannedBook.value = it
                    }
                }
        }
    }

}