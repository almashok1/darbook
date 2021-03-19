package kz.adamant.data.remote.models
import com.google.gson.annotations.SerializedName

data class BookDto(
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val image: String? = null,
    val enabled: Boolean,
    @SerializedName("publish_date")
    val publishedDate: String? = null,
    @SerializedName("genre_id")
    val genreId: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)