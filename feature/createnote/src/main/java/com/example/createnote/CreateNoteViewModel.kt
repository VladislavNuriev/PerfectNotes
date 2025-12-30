package com.example.createnote

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.ContentItem
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
                        val newContent = previousState.content
                            .mapIndexed { index, contentItem ->
                                if (index == command.index && contentItem is ContentItem.Text) {
                                    contentItem.copy(content = command.content)
                                } else {
                                    contentItem
                                }
                            }
                        previousState.copy(
                            content = newContent,
                        )
                    } else {
                        previousState
                    }
                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            title = command.title,
                        )
                    } else {
                        previousState
                    }
                }
            }

            CreateNoteCommand.SaveNote -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is CreateNoteState.Creation) {
                            val title = previousState.title
                            val content = previousState.content.filter { it ->
                                it !is ContentItem.Text || it.content.isNotBlank()
                            }
                            addNote(title = title, content = content)
                            CreateNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            is CreateNoteCommand.AddImage -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        val newItems = previousState.content.toMutableStateList()
                        val lastItem = newItems.last()
                        if (lastItem is ContentItem.Text && lastItem.content.isBlank()) {
                            newItems.removeAt(newItems.lastIndex)
                        }
                        newItems.add(ContentItem.Image(command.uri.toString()))
                        newItems.add(ContentItem.Text(""))
                        previousState.copy(content = newItems)
                    } else {
                        previousState
                    }
                }
            }
        }
    }
}