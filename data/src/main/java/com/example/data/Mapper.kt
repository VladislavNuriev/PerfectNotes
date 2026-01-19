package com.example.data

import com.example.database.models.ContentItemEntity
import com.example.database.models.NoteEntity
import com.example.domain.models.ContentItem
import com.example.domain.models.Note
import kotlinx.serialization.json.Json

fun Note.toNoteEntity(): NoteEntity {
    val contentAsString: String = Json.encodeToString(content.toContentItemEntities())
    return NoteEntity(id, title, contentAsString, updatedAt, isPinned)
}

fun List<ContentItem>.toContentItemEntities(): List<ContentItemEntity> {
    return map { contentItem ->
        when (contentItem) {
            is ContentItem.Image -> ContentItemEntity.Image(contentItem.url)
            is ContentItem.Text -> ContentItemEntity.Text(contentItem.content)
        }
    }
}

fun List<ContentItemEntity>.toContentItems(): List<ContentItem> {
    return map { contentItemEntity ->
        when (contentItemEntity) {
            is ContentItemEntity.Image -> ContentItem.Image(contentItemEntity.url)
            is ContentItemEntity.Text -> ContentItem.Text(contentItemEntity.content)
        }
    }
}

fun NoteEntity.toNote(): Note {
    val contentItemEntities = Json.decodeFromString<List<ContentItemEntity>>(content)
    return Note(id, title, contentItemEntities.toContentItems(), updatedAt, isPinned)
}

fun List<NoteEntity>.toNotes(): List<Note> {
    return this.map { it.toNote() }
}