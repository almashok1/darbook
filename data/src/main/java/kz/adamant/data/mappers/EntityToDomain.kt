package kz.adamant.data.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity
import kz.adamant.data.local.models.ReadingBookEntity
import kz.adamant.data.remote.models.ReadingBookDto
import kz.adamant.data.utils.formatToDate
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

internal fun BookEntity.toDomain(): Book {
    return Book(
        id = bookId,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        publishedDate = publishedDate?.formatToDate(),
        genreId = genreId,
        createdAt = createdAt?.formatToDate(),
        updatedAt = updatedAt?.formatToDate()
    )
}

internal fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = genreId,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt?.formatToDate(),
        updatedAt = updatedAt?.formatToDate()
    )
}

internal fun ReadingBookEntity.toDomain(): ReadingBook {
    return ReadingBook(
        id = readingEntity.bookId,
        userId = readingEntity.userId,
        userContact = readingEntity.userContact,
        userName = readingEntity.userName,
        bookId = readingEntity.bookId,
        startDate = readingEntity.startDate?.formatToDate(),
        endDate = readingEntity.endDate?.formatToDate(),
        createdAt = readingEntity.createdAt?.formatToDate(),
        updatedAt = readingEntity.updatedAt?.formatToDate(),
        book = book?.toDomain()
    )
}

internal fun <Entity, Domain> Flow<List<Entity>>.entityToDomainList(
    mapper: (Entity) -> Domain
): Flow<List<Domain>> {
    return flatMapConcat {
        flowOf(it.map { entity -> mapper(entity) })
    }
}