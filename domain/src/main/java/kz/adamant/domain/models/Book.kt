package kz.adamant.domain.models

data class Book (
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String? = null,
    val publishedDate: String,
    val genreId: Int? = null,
    val createdAt: String,
    val updatedAt: String
)