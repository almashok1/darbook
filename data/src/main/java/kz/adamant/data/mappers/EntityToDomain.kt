package kz.adamant.data.mappers

import com.google.gson.annotations.SerializedName
import kz.adamant.data.common.toDate
import kz.adamant.data.local.models.BookEntity
import kz.adamant.domain.models.Book

internal fun BookEntity.toDomain(): Book {
    return Book(
        id = id,
        isbn = isbn,
        title = title,
        author = author,
        publishedDate = publishedDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}