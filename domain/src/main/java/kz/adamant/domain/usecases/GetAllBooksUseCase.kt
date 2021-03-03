package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository


class GetAllBooksUseCase(
        private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Book>>> {
        return booksRepository.getAllBooks()
    }

    suspend operator fun invoke(query: String?): Flow<Resource<List<Book>>> {
        return if (query == null) invoke()
        else booksRepository.getBooksWithQuery(query)
    }

    suspend operator fun invoke(query: String?, selectedGenres: List<Genre>?): Flow<Resource<List<Book>>> {
        return if (selectedGenres == null) invoke(query)
            else if (selectedGenres.isEmpty()) booksRepository.getAllBooks(false)
            else booksRepository.getBooksWithFilteredGenresId(selectedGenres.map { it.id })
    }
}