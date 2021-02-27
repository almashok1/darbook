package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.remote.models.BookDto

internal fun BookDto.toEntity(): BookEntity {
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