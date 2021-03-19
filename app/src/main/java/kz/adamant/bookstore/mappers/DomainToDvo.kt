package kz.adamant.bookstore.mappers

import kz.adamant.bookstore.models.BookDvo
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource

internal fun Book.toDvo(rentedMark: Int): BookDvo {
    return BookDvo(
        id = id,
        isbn = isbn,
        title = title,
        author = author,
        image = image,
        enabled = enabled,
        publishedDate = publishedDate,
        genreId = genreId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        rentedMark = rentedMark
    )
}

internal suspend fun <Domain, Dvo> Resource<List<Domain>>.toDvo(
    mapper: suspend (List<Domain>?) -> List<Dvo>?
): Resource<List<Dvo>> {
    return when(this) {
        is Resource.Error -> Resource.Error(throwable = this.throwable, data = mapper(this.data))
        is Resource.Loading -> Resource.Loading(data = mapper(this.data))
        is Resource.Success -> Resource.Success(data = mapper(this.data) ?: emptyList())
        else -> Resource.Loading(data = mapper(this.data))
    }
}