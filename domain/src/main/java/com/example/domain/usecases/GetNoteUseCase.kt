package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}