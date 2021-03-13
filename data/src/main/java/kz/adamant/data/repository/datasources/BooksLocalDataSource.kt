package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
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

    override suspend fun getAllReadingBooks(fetchTopN: Int?): Flow<List<ReadingBook>> {
        return if (fetchTopN == null)
            readingBookDao.allReadingBooks().entityToDomainList{ it.toDomain() }
        else
            readingBookDao.topReadingBooks(fetchTopN).entityToDomainList{ it.toDomain() }
    }

    override suspend fun getAllBooksNewlyAdded(fetchTopN: Int?): Flow<List<Book>> {

        return if (fetchTopN == null)
            booksDao.allBooksNewlyAdded().entityToDomainList{ it.toDomain() }
        else
            booksDao.topBooksNewlyAdded(fetchTopN).entityToDomainList{ it.toDomain() }
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

    suspend fun resetReadingTable() {
        readingBookDao.resetTable()
    }

    suspend fun saveAllReadingEntity(readings: List<ReadingEntity>) {
        readingBookDao.insertReadingEntity(readings)
    }


    suspend fun updateOrInsertBook(book: BookEntity) {
        booksDao.updateBook(book)
    }


    private fun getGenreById(id: Int?): GenreEntity? {
        id?.let {
            return genresDao.getGenreById(id)
        }
        return null
    }
}