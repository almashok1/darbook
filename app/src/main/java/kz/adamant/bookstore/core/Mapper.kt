package kz.adamant.bookstore.core

interface Mapper<I, O> {
    fun map(input: I): O
}