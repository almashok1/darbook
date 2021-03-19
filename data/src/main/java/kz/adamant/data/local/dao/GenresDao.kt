package kz.adamant.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.GenreEntity

@Dao
interface GenresDao {
    @Query("SELECT * FROM genres_table")
    fun allGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres_table WHERE genreId = :id")
    fun getGenreById(id: Int): Flow<GenreEntity?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGenre(genre: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres_table")
    suspend fun resetTable()
}