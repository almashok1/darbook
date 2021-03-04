package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre

internal fun BookEntity.toDomain(): Book {
    return Book(
        id = bookId,
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

internal fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = genreId,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}