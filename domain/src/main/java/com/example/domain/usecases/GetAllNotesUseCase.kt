package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}