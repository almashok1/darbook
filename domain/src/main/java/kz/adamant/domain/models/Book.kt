package kz.adamant.domain.models

data class Book (
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String?,
    val publishedDate: String?,
    val genreId: String?,
    val createdAt: String,
    val updatedAt: String
)