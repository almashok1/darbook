package kz.adamant.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.adamant.data.local.models.GenreEntity

@Dao
interface GenresDao {
    @Query("SELECT * FROM genres_table")
    fun allGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres_table WHERE genreId = :id")
    fun getGenreById(id: Int): GenreEntity

    @Insert
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres_table")
    suspend fun resetTable()
}