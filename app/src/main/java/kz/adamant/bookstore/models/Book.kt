package kz.adamant.bookstore.models

data class Book (
    val id: Int,
    val name: String,
    val imageUrl: String? = null,
)