package kz.adamant.bookstore.data.local.entity

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity (
    @PrimaryKey var id: Int,
    var name: String,
    @Nullable
    var imageUrl: String? = null
)