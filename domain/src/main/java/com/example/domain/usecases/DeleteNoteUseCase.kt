package com.example.domain.usecases

import com.example.domain.NotesRepository

class DeleteNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}