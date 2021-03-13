package kz.adamant.data.remote.models

import com.google.gson.annotations.SerializedName

data class ReadingBookDto(
    val id: Int,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_contact")
    val userContact: String? = null,
    @SerializedName("user_name")
    val userName: String? = null,
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("end_date")
    val endDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val book: BookDto? = null
)