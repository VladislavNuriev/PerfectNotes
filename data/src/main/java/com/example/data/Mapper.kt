package com.example.data

import com.example.database.NoteEntity
import com.example.domain.models.Note

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(id,  title, content, updatedAt, isPinned)
}

fun NoteEntity.toNote(): Note {
    return Note(id, title, content, updatedAt, isPinned)
}

fun List<NoteEntity>.toNotes(): List<Note> {
    return this.map { it.toNote() }
}