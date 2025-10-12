package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note

class GetNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}