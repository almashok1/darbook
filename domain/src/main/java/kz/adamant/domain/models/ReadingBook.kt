package kz.adamant.domain.models

import java.util.*

data class ReadingBook(
    val id: Int,
    val userId: String,
    val userContact: String? = null,
    val userName: String? = null,
    val bookId: Int,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val book: Book? = null
)