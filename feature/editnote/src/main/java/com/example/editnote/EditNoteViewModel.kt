package com.example.editnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TestRepositoryImpl
import com.example.domain.models.Note
import com.example.domain.usecases.DeleteNoteUseCase
import com.example.domain.usecases.EditNoteUseCase
import com.example.domain.usecases.GetNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(private val noteId: Int) : ViewModel() {

    private val repo: TestRepositoryImpl = TestRepositoryImpl
    private val editNote: EditNoteUseCase = EditNoteUseCase(repo)
    private val getNote: GetNoteUseCase = GetNoteUseCase(repo)
    private val deleteNote: DeleteNoteUseCase = DeleteNoteUseCase(repo)

    private val _state = MutableStateFlow<EditNoteState>(
        EditNoteState.Initial
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val note = getNote(noteId)
            _state.update {
                EditNoteState.Editing(note)
            }
        }
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newNote = previousState.note.copy(content = command.content)
                        previousState.copy(newNote)
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newNote = previousState.note.copy(title = command.title)
                        previousState.copy(newNote)
                    } else {
                        previousState
                    }
                }
            }

            EditNoteCommand.SaveNote -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            editNote(note)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            deleteNote(note.id)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand {
    data class InputTitle(val title: String) : EditNoteCommand
    data class InputContent(val content: String) : EditNoteCommand
    data object SaveNote : EditNoteCommand
    data object Back : EditNoteCommand
    data object Delete : EditNoteCommand
}

sealed interface EditNoteState {
    data object Initial : EditNoteState
    data class Editing(
        val note: Note
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.title.isNotBlank()
    }

    data object Finished : EditNoteState
}