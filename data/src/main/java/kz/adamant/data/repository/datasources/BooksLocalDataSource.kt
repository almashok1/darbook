package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.local.dao.BooksDao
import kz.adamant.data.local.dao.GenresDao
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre

class BooksLocalDataSource(
    private val booksDao: BooksDao,
    private val genresDao: GenresDao
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        return booksDao.allBooks().flatMapConcat {
            flowOf(it.map { bookEntity ->
                bookEntity.toDomain()
            })
        }
    }

    override suspend fun getAllGenres(): Flow<List<Genre>> {
        return genresDao.allGenres().flatMapConcat {
            flowOf(it.map { genreEntity ->  genreEntity.toDomain() })
        }
    }

    fun getBooksWithQuery(query: String): Flow<List<Book>> {
        return booksDao.searchBooks(query).flatMapConcat {
            flowOf(it.map { bookEntity ->  bookEntity.toDomain() })
        }
    }

    fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<List<Book>> {
        return booksDao.filterBooksByGenresId(selectedGenres).flatMapConcat {
            flowOf(it.map { bookEntity ->  bookEntity.toDomain() })
        }
    }

    fun getBooksWithQueryAndFilter(query: String, selectedGenres: List<Int>): Flow<List<Book>> {
        return booksDao.searchBooksWithFilter(query, selectedGenres).flatMapConcat {
            flowOf(it.map { bookEntity ->  bookEntity.toDomain() })
        }
    }

    suspend fun resetBooksTable() {
        booksDao.resetTable()
    }

    suspend fun saveAllBooks(books: List<BookEntity>) {
        booksDao.insertBooks(books)
    }

    suspend fun resetGenresTable() {
        genresDao.resetTable()
    }

    suspend fun saveAllGenres(genres: List<GenreEntity>) {
        genresDao.insertGenres(genres)
    }

    private fun getGenreById(id: Int?): GenreEntity? {
        id?.let {
            return genresDao.getGenreById(id)
        }
        return null
    }
}