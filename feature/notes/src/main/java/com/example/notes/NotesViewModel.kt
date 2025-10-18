@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NotesRepositoryImpl
import com.example.data.TestRepositoryImpl
import com.example.domain.models.Note
import com.example.domain.usecases.GetAllNotesUseCase
import com.example.domain.usecases.SearchNotesUseCase
import com.example.domain.usecases.SwitchPinnedStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(context: Context) : ViewModel() {
    private val repo = NotesRepositoryImpl.getInstance(context)
    private val getAllNotes: GetAllNotesUseCase = GetAllNotesUseCase(repo)
    private val searchNotes = SearchNotesUseCase(repo)
    private val switchPinnedStatus = SwitchPinnedStatusUseCase(repo)
    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")


    init {
        query
            .onEach { input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNotes()
                } else {
                    searchNotes(input)
                }
            }
            .onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val unPinnedNotes = notes.filter { !it.isPinned }
                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = unPinnedNotes) }
            }.launchIn(viewModelScope)

    }


    fun processCommand(command: NotesCommand) {
        viewModelScope.launch {
            when (command) {
                is NotesCommand.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }

                is NotesCommand.SwitchPinnedStatus -> {
                    switchPinnedStatus(command.noteId)
                }
            }
        }
    }
}

sealed interface NotesCommand {
    data class InputSearchQuery(val query: String) : NotesCommand
    data class SwitchPinnedStatus(val noteId: Int) : NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)