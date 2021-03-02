package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kz.adamant.data.mappers.toEntity
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.utils.networkBoundResource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val localDataSource: BooksDataSource,
    private val remoteDataSource: BooksDataSource,
): BooksRepository {
    override suspend fun getAllBooks(): Flow<Resource<List<Book>>> {
        return networkBoundResource(
            query = { localDataSource.getAllBooks() },
            fetch = { remoteDataSource.getAllBooks() },
            saveFetchResult = { items -> items.collect { saveToDatabase(it) } },
        )
    }

    private suspend fun saveToDatabase(books: List<Book>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetBooksTable()
        local.saveAllBooks(books.map {bookDto -> bookDto.toEntity()})
    }
}