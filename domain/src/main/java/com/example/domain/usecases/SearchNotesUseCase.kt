package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.Note
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}