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

    @Query("SELECT * FROM books_table WHERE LOWER(title) LIKE LOWER(:query)")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE genreId IN (:genres)")
    fun filterBooksByGenresId(genres: List<Int>): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE genreId IN (:genres) AND LOWER(title) LIKE LOWER(:query)")
    fun searchBooksWithFilter(query: String, genres: List<Int>): Flow<List<BookEntity>>

    @Insert
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books_table")
    suspend fun resetTable()
}