package kz.adamant.data.mappers

import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.local.models.ReadingEntity
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

internal fun Book.toEntity(): BookEntity {
    return BookEntity(
        bookId = id,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        enabled = enabled,
        publishedDate = publishedDate?.time,
        genreId = genreId,
        createdAt = createdAt?.time,
        updatedAt = updatedAt?.time
    )
}

internal fun Genre.toEntity(): GenreEntity {
    return GenreEntity(
        genreId = id,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt?.time,
        updatedAt = updatedAt?.time,
    )
}


internal fun ReadingBook.toEntity(): ReadingEntity {
    return ReadingEntity(
        readingId = id,
        userId = userId,
        userContact = userContact,
        userName = userName,
        bookId = bookId,
        startDate = startDate?.time,
        endDate = endDate?.time,
        createdAt = createdAt?.time,
        updatedAt = updatedAt?.time,
    )
}