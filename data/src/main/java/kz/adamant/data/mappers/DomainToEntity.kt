package kz.adamant.data.mappers

import kz.adamant.data.common.formatTo
import kz.adamant.data.local.models.BookEntity
import kz.adamant.domain.models.Book

internal fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        isbn = isbn,
        title = title,
        author = author,
        publishedDate = publishedDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}