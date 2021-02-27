package kz.adamant.data.repository

import kotlinx.coroutines.flow.*
import kz.adamant.data.mappers.toEntity

import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryIml(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource,
): BooksRepository {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        val localBooks = localDataSource.getAllBooks()
        val dbRes = localBooks.first()
        return if (dbRes.isEmpty()) {
            val response = remoteDataSource.getAllBooks()
            val apiRes = response.first()
            saveToDatabase(apiRes)
            flowOf(apiRes)
        } else {
            flowOf(dbRes)
        }
    }

    private suspend fun saveToDatabase(books: List<Book>?) {
        books?.let {
            localDataSource.resetBooksTable()
            localDataSource.saveAllBooks(it.map {bookDto -> bookDto.toEntity()})
        }
    }
}