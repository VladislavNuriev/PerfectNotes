package com.example.createnote

import android.net.Uri

sealed interface CreateNoteCommand {
    data class InputTitle(val title: String) : CreateNoteCommand
    data class InputContent(val content: String, val index: Int) : CreateNoteCommand
    data class AddImage(val uri: Uri) : CreateNoteCommand
    data object SaveNote : CreateNoteCommand
    data object Back : CreateNoteCommand
}