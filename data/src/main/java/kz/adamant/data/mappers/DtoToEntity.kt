package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.remote.models.BookDto
import kz.adamant.data.remote.models.GenreDto

internal fun BookDto.toEntity(): BookEntity {
    return BookEntity(
        bookId = id,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        enabled = enabled,
        publishedDate = publishedDate?.toLong(),
        genreId = genreId,
        createdAt = createdAt?.toLong(),
        updatedAt = updatedAt?.toLong()
    )
}

internal fun GenreDto.toEntity(): GenreEntity {
    return GenreEntity(
        genreId = id,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt?.toLong(),
        updatedAt = updatedAt?.toLong()
    )
}