package kz.adamant.domain.models

data class RentInfo(
    val userId: String,
    val userName: String,
    val userContact: String,
    val bookId: Int,
    val startDate: String,
)