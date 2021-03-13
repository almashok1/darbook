package kz.adamant.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.local.models.ReadingBookEntity
import kz.adamant.data.local.models.ReadingEntity

@Dao
interface ReadingBooksDao {
    @Query("SELECT * FROM reading_book_table")
    fun allReadingBooks(): Flow<List<ReadingBookEntity>>

    @Query("SELECT * FROM reading_book_table ORDER BY updatedAt DESC LIMIT :topN")
    fun topReadingBooks(topN: Int): Flow<List<ReadingBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingEntity(genres: List<ReadingEntity>)

    @Query("DELETE FROM reading_book_table")
    suspend fun resetTable()
}