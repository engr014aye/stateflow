package com.saddiqueini.slateflow

import android.app.Application
import com.saddiqueini.slateflow.data.local.SlateFlowDatabase
import com.saddiqueini.slateflow.data.repository.NoteRepository
import com.saddiqueini.slateflow.data.repository.NoteRepositoryImpl
import com.saddiqueini.slateflow.data.repository.TaskRepository
import com.saddiqueini.slateflow.data.repository.TaskRepositoryImpl
import com.saddiqueini.slateflow.domain.NoteUseCases
import com.saddiqueini.slateflow.domain.TaskUseCases
import com.saddiqueini.slateflow.ui.common.SlateFlowViewModelFactory

class SlateFlowApp : Application() {

    lateinit var database: SlateFlowDatabase
        private set

    lateinit var noteRepository: NoteRepository
        private set

    lateinit var taskRepository: TaskRepository
        private set

    lateinit var noteUseCases: NoteUseCases
        private set

    lateinit var taskUseCases: TaskUseCases
        private set

    lateinit var viewModelFactory: SlateFlowViewModelFactory
        private set

    override fun onCreate() {
        super.onCreate()

        database = SlateFlowDatabase.getDatabase(this)
        noteRepository = NoteRepositoryImpl(database.noteDao())
        taskRepository = TaskRepositoryImpl(database.taskDao())

        noteUseCases = NoteUseCases(noteRepository)
        taskUseCases = TaskUseCases(taskRepository)

        viewModelFactory = SlateFlowViewModelFactory(noteUseCases, taskUseCases)
    }
}
