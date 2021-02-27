package kz.adamant.data.local.models

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "books_table")
data class BookEntity (
    @PrimaryKey
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val publishedDate: String,
    val createdAt: String,
    val updatedAt: String
)