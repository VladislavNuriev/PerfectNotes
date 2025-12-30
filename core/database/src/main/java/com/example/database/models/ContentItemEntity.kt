package com.example.database.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentItemEntity {
    @Serializable
    data class Text(val content: String): ContentItemEntity
    @Serializable
    data class Image(val url: String): ContentItemEntity
}