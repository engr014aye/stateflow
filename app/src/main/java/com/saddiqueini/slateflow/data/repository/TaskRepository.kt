package com.saddiqueini.slateflow.data.repository

import com.saddiqueini.slateflow.data.local.TaskDao
import com.saddiqueini.slateflow.data.model.TaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskEntity>>
    fun getTasksByCompletion(completed: Boolean): Flow<List<TaskEntity>>
    fun searchTasks(query: String): Flow<List<TaskEntity>>
    suspend fun getTaskById(id: Long): TaskEntity?
    suspend fun insertTask(task: TaskEntity): Long
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun toggleTaskCompletion(id: Long, currentCompletedStatus: Boolean)
}

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskEntity>> =
        taskDao.getAllTasks().flowOn(ioDispatcher)

    override fun getTasksByCompletion(completed: Boolean): Flow<List<TaskEntity>> =
        taskDao.getTasksByCompletion(completed).flowOn(ioDispatcher)

    override fun searchTasks(query: String): Flow<List<TaskEntity>> =
        taskDao.searchTasks(query).flowOn(ioDispatcher)

    override suspend fun getTaskById(id: Long): TaskEntity? =
        withContext(ioDispatcher) {
            taskDao.getTaskById(id)
        }

    override suspend fun insertTask(task: TaskEntity): Long =
        withContext(ioDispatcher) {
            taskDao.insertTask(task)
        }

    override suspend fun updateTask(task: TaskEntity) =
        withContext(ioDispatcher) {
            taskDao.updateTask(task)
        }

    override suspend fun deleteTask(task: TaskEntity) =
        withContext(ioDispatcher) {
            taskDao.deleteTask(task)
        }

    override suspend fun toggleTaskCompletion(id: Long, currentCompletedStatus: Boolean) =
        withContext(ioDispatcher) {
            val nextStatus = !currentCompletedStatus
            val completedTime = if (nextStatus) System.currentTimeMillis() else null
            taskDao.updateTaskCompletion(id, nextStatus, completedTime)
        }
}
