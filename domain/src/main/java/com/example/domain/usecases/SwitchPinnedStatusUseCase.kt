package com.example.domain.usecases

import com.example.domain.NotesRepository
import javax.inject.Inject

class SwitchPinnedStatusUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}