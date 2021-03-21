package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.local.dao.BooksDao
import kz.adamant.data.local.dao.GenresDao
import kz.adamant.data.local.dao.ReadingBooksDao
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.local.models.ReadingEntity
import kz.adamant.data.mappers.entityToDomainList
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.utils.RENTED_BY_ANOTHER_USER
import kz.adamant.domain.utils.RENTED_BY_CURRENT_USER
import kz.adamant.domain.utils.RENTED_BY_NONE

class BooksLocalDataSource(
    private val booksDao: BooksDao,
    private val genresDao: GenresDao,
    private val readingBookDao: ReadingBooksDao,
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        return booksDao.allBooks().entityToDomainList{ it.toDomain() }
    }

    override suspend fun getAllGenres(): Flow<List<Genre>> {
        return genresDao.allGenres().entityToDomainList{ it.toDomain() }
    }

    override suspend fun getGenreById(genreId: Int): Flow<Genre?> {
        return genresDao.getGenreById(genreId).flatMapConcat { flowOf(it?.toDomain()) }
    }

    override suspend fun getAllReadingBooks(): Flow<List<ReadingBook>> {
        return readingBookDao.allReadingBooks().entityToDomainList{ it.toDomain() }
    }

    override suspend fun getAllBooksNewlyAdded(): Flow<List<Book>> {
        return booksDao.allBooksNewlyAdded().entityToDomainList{ it.toDomain() }
    }

    fun getBooksWithQuery(query: String): Flow<List<Book>> {
        return booksDao.searchBooks(query).entityToDomainList{ it.toDomain() }
    }

    fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<List<Book>> {
        return booksDao.filterBooksByGenresId(selectedGenres).entityToDomainList{ it.toDomain() }
    }

    fun getBooksWithQueryAndFilter(query: String, selectedGenres: List<Int>): Flow<List<Book>> {
        return booksDao.searchBooksWithFilter(query, selectedGenres).entityToDomainList{ it.toDomain() }
    }

    suspend fun getBookRentingMark(bookId: Int, userId: String): Int {
        return readingBookDao.getReadingBookCountByUserId(bookId, userId).run {
            if (this > 0) RENTED_BY_CURRENT_USER
            else readingBookDao.getReadingBookCount(bookId).run {
                if (this > 0) RENTED_BY_ANOTHER_USER else RENTED_BY_NONE
            }
        }
    }

    fun getBookByIsbn(isbn: String): Flow<List<Book>>{
        return booksDao.getBookByIsbn(isbn).entityToDomainList { it.toDomain() }
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

    suspend fun updateGenre(genre: GenreEntity) {
        genresDao.updateGenre(genre)
    }

    suspend fun resetReadingTable() {
        readingBookDao.resetTable()
    }

    suspend fun saveAllReadingEntity(readings: List<ReadingEntity>) {
        readingBookDao.insertReadingEntity(readings)
    }


    suspend fun updateOrInsertBook(book: BookEntity) {
        booksDao.updateBook(book)
    }
}