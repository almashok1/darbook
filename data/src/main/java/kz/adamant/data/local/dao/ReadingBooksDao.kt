package kz.adamant.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.local.models.ReadingBookEntity
import kz.adamant.data.local.models.ReadingEntity

@Dao
interface ReadingBooksDao {
    @Query("SELECT * FROM reading_book_table ORDER BY updatedAt DESC")
    fun allReadingBooks(): Flow<List<ReadingBookEntity>>

    @Query("SELECT * FROM reading_book_table ORDER BY updatedAt DESC LIMIT :topN")
    fun topReadingBooks(topN: Int): Flow<List<ReadingBookEntity>>

    @Query("SELECT COUNT(*) FROM reading_book_table WHERE bookId = :bookId AND userId = :userId")
    fun getReadingBookCountByUserId(bookId: Int, userId: String): Int

    @Query("SELECT COUNT(*) FROM reading_book_table WHERE bookId = :bookId")
    suspend fun getReadingBookCount(bookId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingEntity(genres: List<ReadingEntity>)

    @Query("DELETE FROM reading_book_table")
    suspend fun resetTable()
}