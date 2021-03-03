package kz.adamant.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity (
    @PrimaryKey
    val bookId: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String? = null,
    val publishedDate: String,
    val genreId: Int? = null,
    val createdAt: String,
    val updatedAt: String
)