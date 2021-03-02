package kz.adamant.data.remote.models
import com.google.gson.annotations.SerializedName

data class BookDto(
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String?,
    @SerializedName("publish_date")
    val publishedDate: String?,
    @SerializedName("genre_id")
    val genreId: String?,
    val createdAt: String,
    val updatedAt: String
)