package kz.adamant.bookstore.states

import kz.adamant.domain.models.Book

sealed class BooksViewState {
    object Loading: BooksViewState()
    data class Loaded(val data: List<Book>): BooksViewState()
    data class Error(val message: String)
}