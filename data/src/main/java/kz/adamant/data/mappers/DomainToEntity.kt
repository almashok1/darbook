package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.domain.models.Book

internal fun Book.toEntity(): BookEntity {
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