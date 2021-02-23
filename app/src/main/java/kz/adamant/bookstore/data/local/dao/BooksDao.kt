package kz.adamant.bookstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.adamant.bookstore.data.local.entity.BookEntity

@Dao
interface BooksDao {
    @Query("SELECT * FROM books_table")
    fun allBooks(): Flow<List<BookEntity>>
}