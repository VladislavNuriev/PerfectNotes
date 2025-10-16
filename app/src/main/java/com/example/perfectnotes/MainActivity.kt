package com.example.perfectnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.createnote.CreateNoteScreen
import com.example.editnote.EditNoteScreen
import com.example.ui.PerfectNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerfectNotesTheme {
                EditNoteScreen(noteId = 5,
                    onFinished = {Log.d("MainActivity", "finished")})
                //CreateNoteScreen(onFinished = { Log.d("MainActivity", "finished") })
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClicked: ${it.id} ")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "onFloatingButtonClick: ")
//                    }
//                )
            }
        }
    }
}