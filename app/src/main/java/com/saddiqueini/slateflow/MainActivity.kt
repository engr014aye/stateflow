package com.saddiqueini.slateflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.saddiqueini.slateflow.ui.MainSlateScreen
import com.saddiqueini.slateflow.ui.notes.NotesViewModel
import com.saddiqueini.slateflow.ui.tasks.TasksViewModel
import com.saddiqueini.slateflow.ui.theme.SlateFlowTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable complete edge-to-edge system display
        enableEdgeToEdge()

        val app = application as SlateFlowApp
        val factory = app.viewModelFactory
        val notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]
        val tasksViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]

        setContent {
            SlateFlowTheme {
                MainSlateScreen(
                    notesViewModel = notesViewModel,
                    tasksViewModel = tasksViewModel
                )
            }
        }
    }
}
