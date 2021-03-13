package kz.adamant.data.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.remote.models.BookDto
import kz.adamant.data.remote.models.GenreDto
import kz.adamant.data.remote.models.ReadingBookDto
import kz.adamant.data.utils.toDate
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

internal fun BookDto.toDomain(): Book {
    return Book(
        id = id,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        publishedDate = publishedDate?.toDate(),
        genreId = genreId,
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
    )
}

internal fun GenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        title = title,
        sort = sort,
        enabled = enabled,
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
    )
}

internal fun ReadingBookDto.toDomain(): ReadingBook {
    return ReadingBook(
        id = id,
        userId = userId,
        userContact = userContact,
        userName = userName,
        bookId = bookId,
        startDate = startDate?.toDate(),
        endDate = endDate?.toDate(),
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
        book = book?.toDomain()
    )
}

internal fun <Dto, Domain> Flow<List<Dto>>.dtoToDomainList(
    mapper: (Dto) -> Domain
): Flow<List<Domain>> {
    return flatMapConcat {
        flowOf(it.map { dto -> mapper(dto) })
    }
}