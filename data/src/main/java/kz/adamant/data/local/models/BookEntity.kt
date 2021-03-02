package kz.adamant.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity (
    @PrimaryKey
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String?,
    val publishedDate: String?,
    val genreId: String?,
    val createdAt: String,
    val updatedAt: String
)