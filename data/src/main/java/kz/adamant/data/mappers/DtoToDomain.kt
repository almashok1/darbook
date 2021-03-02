package kz.adamant.data.mappers

import kz.adamant.data.remote.models.BookDto
import kz.adamant.domain.models.Book

internal fun BookDto.toDomain(): Book {
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