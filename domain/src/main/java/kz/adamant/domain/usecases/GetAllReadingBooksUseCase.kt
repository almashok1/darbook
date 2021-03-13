package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.utils.TOP_N

class GetAllReadingBooksUseCase(
    private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(shouldFetchFromNetwork: Boolean, fetchTopN: Int? = TOP_N): Flow<Resource<List<ReadingBook>>> {
        return booksRepository.getAllReadingBooks(shouldFetchFromNetwork, fetchTopN)
    }
}