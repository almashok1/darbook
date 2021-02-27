package kz.adamant.data.repository

import kotlinx.coroutines.flow.*
import kz.adamant.data.mappers.toDomain

import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryIml(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource,
): BooksRepository {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        return remoteDataSource.getAllBooks()
    }

}