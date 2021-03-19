package kz.adamant.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.BookEntity

@Dao
interface BooksDao {
    @Query("SELECT * FROM books_table")
    fun allBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table ORDER BY updatedAt DESC")
    fun allBooksNewlyAdded(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table ORDER BY updatedAt DESC LIMIT :topN")
    fun topBooksNewlyAdded(topN: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE LOWER(title) LIKE LOWER(:query) OR isbn LIKE (:query)")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE genreId IN (:genres)")
    fun filterBooksByGenresId(genres: List<Int>): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE genreId IN (:genres) AND (LOWER(title) LIKE LOWER(:query) OR isbn LIKE (:query))")
    fun searchBooksWithFilter(query: String, genres: List<Int>): Flow<List<BookEntity>>

    @Query("SELECT * FROM books_table WHERE isbn = :isbn")
    fun getBookByIsbn(isbn: String): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books_table")
    suspend fun resetTable()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBook(book: BookEntity)
}