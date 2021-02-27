package kz.adamant.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.BookEntity

@Dao
interface BooksDao {
    @Query("SELECT * FROM books_table")
    fun allBooks(): Flow<List<BookEntity>>
}