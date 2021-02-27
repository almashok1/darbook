package kz.adamant.domain.models

import java.util.*

data class Book (
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val publishedDate: String,
    val createdAt: String,
    val updatedAt: String
)