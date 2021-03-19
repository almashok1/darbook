package kz.adamant.domain.models

import java.util.*

data class Book (
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String? = null,
    val enabled: Boolean,
    val publishedDate: Date? = null,
    val genreId: Int? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)