package com.example.domain.usecases

import com.example.domain.NotesRepository
import com.example.domain.models.ContentItem
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(title: String, content: List<ContentItem>) {
        repository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updatedAt = System.currentTimeMillis()
        )
    }
}