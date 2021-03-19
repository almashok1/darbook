package kz.adamant.bookstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class BookDvo(
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
    val rentedMark: Int
) : Parcelable
