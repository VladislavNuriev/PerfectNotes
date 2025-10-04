package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note

class AddNoteUseCase(private val repository: NotesRepository) {
    operator fun invoke(note: Note) {
        repository.addNote(note)
    }
}