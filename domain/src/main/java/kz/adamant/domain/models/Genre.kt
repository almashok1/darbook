package kz.adamant.domain.models

data class Genre (
    val id: Int,
    val title: String,
    val sort: Int,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
) {
    var selected: Boolean = false
}