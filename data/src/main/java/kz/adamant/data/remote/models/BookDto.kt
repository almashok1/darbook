package kz.adamant.data.remote.models
import com.google.gson.annotations.SerializedName

data class BookDto(
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    @SerializedName("publish_date")
    val publishedDate: String,
    val createdAt: String,
    val updatedAt: String
)