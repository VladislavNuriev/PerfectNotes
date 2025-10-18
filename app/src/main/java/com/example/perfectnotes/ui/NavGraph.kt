package com.example.perfectnotes.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.createnote.CreateNoteScreen
import com.example.editnote.EditNoteScreen
import com.example.notes.ui.NotesScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ) {
        composable(Screen.Notes.route) {
            NotesScreen(
                onNoteClick = {
                    navController.navigate(Screen.EditNote.createRoute(it.id))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote.route)
                }
            )
        }
        composable(route = Screen.CreateNote.route) {
            CreateNoteScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.EditNote.route) {
            val noteId = Screen.EditNote.getNoteId(it.arguments)
            EditNoteScreen(
                noteId = noteId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object Notes : Screen("notes")

    data object CreateNote : Screen("create_note")

    data object EditNote : Screen("edit_note/{note_id}") {
        fun createRoute(noteId: Int): String {
            return "edit_note/$noteId"
        }

        fun getNoteId(arguments: Bundle?): Int {
            return arguments?.getString("note_id")?.toInt() ?: 0
        }

    }
}