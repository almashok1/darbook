package kz.adamant.data.remote.models

import com.google.gson.annotations.SerializedName

data class RentInfoDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_contact")
    val userContact: String,
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("start_date")
    val startDate: String,
)