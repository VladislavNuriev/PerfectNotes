package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repository.editNote(
            note.copy(updatedAt = System.currentTimeMillis())
        )
    }
}