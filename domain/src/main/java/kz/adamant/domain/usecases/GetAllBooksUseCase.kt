package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.utils.filterThenMap


class GetAllBooksUseCase(
        private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(query: String?, selectedGenres: List<Genre>?, shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>> {
        return booksRepository.getAllBooks(
            query,
            selectedGenres?.filterThenMap(
                predicate = {genre -> genre.selected},
                transform = {genre -> genre.id}
            ),
            shouldFetchFromNetwork
        )
    }
}