package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class GetAllBooksNewlyAddedUseCase(
    private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>> {
        return booksRepository.getAllBooksNewlyAdded(shouldFetchFromNetwork)
    }
}