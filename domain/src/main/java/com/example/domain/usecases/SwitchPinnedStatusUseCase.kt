package com.example.domain.usecases

import com.example.domain.NotesRepository

class SwitchPinnedStatusUseCase(private val repository: NotesRepository) {
    operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}