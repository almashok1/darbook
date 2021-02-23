package kz.adamant.bookstore.data.repository.datasources.mappers

import kz.adamant.bookstore.core.Mapper
import kz.adamant.bookstore.data.local.entity.BookEntity
import kz.adamant.bookstore.models.Book

class BookEntityToBookMapper: Mapper<BookEntity, Book> {
    override fun map(input: BookEntity): Book {
        return Book(
            input.id,
            input.name,
            input.imageUrl
        )
    }
}