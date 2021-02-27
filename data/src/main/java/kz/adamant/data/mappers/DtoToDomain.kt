package kz.adamant.data.mappers

import kz.adamant.data.remote.models.BookDto
import kz.adamant.domain.models.Book

internal fun BookDto.toDomain(): Book {
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