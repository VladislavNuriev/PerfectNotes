package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note

class EditNoteUseCase(private val repository: NotesRepository) {
    operator fun invoke(note: Note) {
        repository.editNote(note)
    }
}