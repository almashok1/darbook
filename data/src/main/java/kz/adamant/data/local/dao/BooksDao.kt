package kz.adamant.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.BookEntity

@Dao
interface BooksDao {
    @Query("SELECT * FROM books_table")
    fun allBooks(): Flow<List<BookEntity>>

    @Insert
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books_table")
    suspend fun resetTable()
}