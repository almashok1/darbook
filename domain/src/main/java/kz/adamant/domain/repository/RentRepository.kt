package kz.adamant.domain.repository

import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.RentInfo

interface RentRepository{
    suspend fun getBookRentingMark(bookId: Int, userId: String): Int
    suspend fun rentBook(rentInfo: RentInfo): ReadingBook?
}