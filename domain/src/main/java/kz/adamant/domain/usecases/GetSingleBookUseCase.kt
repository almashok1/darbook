package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.repository.BooksRepository

class GetSingleBookUseCase(
    private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(isbn: String): Flow<List<Book>> {
        return booksRepository.getBookByIsbn(isbn)
    }
}