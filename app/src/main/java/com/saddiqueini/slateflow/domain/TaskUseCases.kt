package com.saddiqueini.slateflow.domain

import com.saddiqueini.slateflow.data.model.TaskEntity
import com.saddiqueini.slateflow.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskUseCases(
    private val repository: TaskRepository
) {
    fun getTasks(query: String = ""): Flow<List<TaskEntity>> {
        return if (query.isBlank()) {
            repository.getAllTasks()
        } else {
            repository.searchTasks(query.trim())
        }
    }

    suspend fun saveTask(task: TaskEntity): Long {
        return if (task.id == 0L) {
            repository.insertTask(task)
        } else {
            repository.updateTask(task)
            task.id
        }
    }

    suspend fun deleteTask(task: TaskEntity) {
        repository.deleteTask(task)
    }

    suspend fun toggleTaskCompletion(id: Long, currentCompletedStatus: Boolean) {
        repository.toggleTaskCompletion(id, currentCompletedStatus)
    }
}
