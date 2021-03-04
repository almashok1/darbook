package kz.adamant.data.mappers

import kz.adamant.data.remote.models.BookDto
import kz.adamant.data.remote.models.GenreDto
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre

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

internal fun GenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}