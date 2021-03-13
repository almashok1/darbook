package kz.adamant.data.local.models

import androidx.room.*

@Entity(tableName = "reading_book_table")
data class ReadingEntity (
    @PrimaryKey
    val readingId: Int,
    val userId: String,
    val userContact: String? = null,
    val userName: String? = null,
    val bookId: Int,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
)


data class ReadingBookEntity(
    @Embedded val readingEntity: ReadingEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId",
        entity = BookEntity::class
    )
    val book: BookEntity?
)