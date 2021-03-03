package kz.adamant.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table")
data class GenreEntity(
    @PrimaryKey
    val genreId: Int,
    val title: String,
    val sort: Int,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String
)
