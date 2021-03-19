package kz.adamant.data.repository

import kz.adamant.data.mappers.toDto
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.RentInfo
import kz.adamant.domain.repository.RentRepository

class RentRepositoryImpl(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource
): RentRepository {
    override suspend fun getBookRentingMark(bookId: Int, userId: String): Int {
        return localDataSource.getBookRentingMark(bookId, userId)
    }

    override suspend fun rentBook(rentInfo: RentInfo): ReadingBook? {
        return remoteDataSource.rentBook(rentInfo.toDto())
    }
}