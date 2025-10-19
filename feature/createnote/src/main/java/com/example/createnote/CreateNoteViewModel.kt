package com.example.createnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val addNote: AddNoteUseCase) : ViewModel() {

    private val _state = MutableStateFlow<CreateNoteState>(
        CreateNoteState.Creation()
    )
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            CreateNoteCommand.Back -> {
                _state.update { CreateNoteState.Finished }
            }

            is CreateNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            content = command.content,
                            isSaveEnabled = (previousState.content.isNotBlank() && previousState.title.isNotBlank())
                        )
                    } else {
                        CreateNoteState.Creation(content = command.content)
                    }

                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            title = command.title,
                            isSaveEnabled = (previousState.title.isNotBlank() && previousState.content.isNotBlank())
                        )
                    } else {
                        CreateNoteState.Creation(title = command.title)
                    }
                }
            }

            CreateNoteCommand.SaveNote -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is CreateNoteState.Creation) {
                            val title = previousState.title
                            val content = previousState.content
                            addNote(title = title, content = content)
                            CreateNoteState.Finished
                        } else {
                            CreateNoteState.Finished
                        }
                    }
                }
            }
        }
    }
}

sealed interface CreateNoteCommand {
    data class InputTitle(val title: String) : CreateNoteCommand
    data class InputContent(val content: String) : CreateNoteCommand
    data object SaveNote : CreateNoteCommand
    data object Back : CreateNoteCommand
}

sealed interface CreateNoteState {
    data class Creation(
        val title: String = "",
        val content: String = "",
        val isSaveEnabled: Boolean = false
    ) : CreateNoteState

    data object Finished : CreateNoteState
}