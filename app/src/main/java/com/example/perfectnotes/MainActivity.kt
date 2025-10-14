package com.example.perfectnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.createnote.ui.CreateNoteScreen
import com.example.notes.ui.NotesScreen
import com.example.ui.PerfectNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerfectNotesTheme {
                CreateNoteScreen(onFinished = { Log.d("MainActivity", "finished") })
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