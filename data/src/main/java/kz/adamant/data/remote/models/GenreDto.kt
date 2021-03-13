package kz.adamant.data.remote.models

data class GenreDto (
    val id: Int,
    val title: String,
    val sort: Int,
    val enabled: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)