package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.remote.models.BookDto

internal fun BookDto.toEntity(): BookEntity {
    return BookEntity(
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