package com.saddiqueini.slateflow.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saddiqueini.slateflow.domain.NoteUseCases
import com.saddiqueini.slateflow.domain.TaskUseCases
import com.saddiqueini.slateflow.ui.notes.NotesViewModel
import com.saddiqueini.slateflow.ui.tasks.TasksViewModel

class SlateFlowViewModelFactory(
    private val noteUseCases: NoteUseCases,
    private val taskUseCases: TaskUseCases
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
                NotesViewModel(noteUseCases) as T
            }
            modelClass.isAssignableFrom(TasksViewModel::class.java) -> {
                TasksViewModel(taskUseCases) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
