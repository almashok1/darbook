package kz.adamant.bookstore.data.repository

import kotlinx.coroutines.flow.*
import kz.adamant.bookstore.models.Book

import kz.adamant.bookstore.data.repository.datasources.BooksLocalDataSource
import kz.adamant.bookstore.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.bookstore.data.repository.datasources.mappers.BookEntityToBookMapper

class BooksRepositoryIml(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource,
    private val mapper: BookEntityToBookMapper
): BooksRepository {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        return localDataSource.getAllBooks()
            .flatMapConcat {
                flowOf(it.map { bookEntity -> mapper.map(bookEntity) })
            }
    }

}