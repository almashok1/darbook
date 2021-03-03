package kz.adamant.data.mappers

import kz.adamant.data.local.models.*
import kz.adamant.domain.models.*

internal fun Book.toEntity(): BookEntity {
    return BookEntity(
        bookId = id,
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

internal fun Genre.toEntity(): GenreEntity {
    return GenreEntity(
        genreId = id,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}