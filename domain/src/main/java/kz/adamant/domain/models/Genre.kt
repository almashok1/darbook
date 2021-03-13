package kz.adamant.domain.models

import java.util.*

data class Genre (
    val id: Int,
    val title: String,
    val sort: Int,
    val enabled: Boolean,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)