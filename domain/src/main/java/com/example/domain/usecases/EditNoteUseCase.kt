package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note

class EditNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repository.editNote(
            note.copy(updatedAt = System.currentTimeMillis())
        )
    }
}