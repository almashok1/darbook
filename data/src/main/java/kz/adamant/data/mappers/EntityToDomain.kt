package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.domain.models.Book

internal fun BookEntity.toDomain(): Book {
    return Book(
        id = id,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        publishedDate = publishedDate,
        genreId = genreId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}