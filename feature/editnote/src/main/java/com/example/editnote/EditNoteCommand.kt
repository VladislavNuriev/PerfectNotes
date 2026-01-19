package com.example.editnote

import android.net.Uri
import com.example.domain.models.ContentItem

sealed interface EditNoteCommand {
    data class InputTitle(val title: String) : EditNoteCommand
    data class InputContent(val content: String, val index: Int) : EditNoteCommand
    data class AddImage(val uri: Uri) : EditNoteCommand
    data class DeleteImage(val index: Int): EditNoteCommand
    data object SaveNote : EditNoteCommand
    data object Back : EditNoteCommand
    data object Delete : EditNoteCommand
}