package com.saddiqueini.slateflow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddiqueini.slateflow.data.model.Priority
import com.saddiqueini.slateflow.data.model.TaskEntity
import com.saddiqueini.slateflow.domain.TaskUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModel(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _priorityFilter = MutableStateFlow<Priority?>(null)
    val priorityFilter: StateFlow<Priority?> = _priorityFilter.asStateFlow()

    private val _isCompletedExpanded = MutableStateFlow(true)
    val isCompletedExpanded: StateFlow<Boolean> = _isCompletedExpanded.asStateFlow()

    private val _isEditorOpen = MutableStateFlow(false)
    val isEditorOpen: StateFlow<Boolean> = _isEditorOpen.asStateFlow()

    private val _editingTask = MutableStateFlow<TaskEntity?>(null)
    val editingTask: StateFlow<TaskEntity?> = _editingTask.asStateFlow()

    private val _recentlyDeletedTask = MutableStateFlow<TaskEntity?>(null)
    val recentlyDeletedTask: StateFlow<TaskEntity?> = _recentlyDeletedTask.asStateFlow()

    private val allTasksFlow = _searchQuery
        .flatMapLatest { query ->
            taskUseCases.getTasks(query)
        }

    val activeTasks: StateFlow<List<TaskEntity>> = combine(
        allTasksFlow,
        _priorityFilter
    ) { tasks, filter ->
        tasks.filter { !it.isCompleted && (filter == null || it.priority == filter) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val completedTasks: StateFlow<List<TaskEntity>> = combine(
        allTasksFlow,
        _priorityFilter
    ) { tasks, filter ->
        tasks.filter { it.isCompleted && (filter == null || it.priority == filter) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun setPriorityFilter(priority: Priority?) {
        _priorityFilter.value = priority
    }

    fun toggleCompletedExpanded() {
        _isCompletedExpanded.value = !_isCompletedExpanded.value
    }

    fun openCreateTask() {
        _editingTask.value = null
        _isEditorOpen.value = true
    }

    fun openEditTask(task: TaskEntity) {
        _editingTask.value = task
        _isEditorOpen.value = true
    }

    fun closeEditor() {
        _isEditorOpen.value = false
        _editingTask.value = null
    }

    fun saveTask(title: String, description: String, priority: Priority) {
        viewModelScope.launch {
            val current = _editingTask.value
            val taskToSave = if (current != null) {
                current.copy(
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority
                )
            } else {
                TaskEntity(
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis()
                )
            }
            taskUseCases.saveTask(taskToSave)
            closeEditor()
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            taskUseCases.toggleTaskCompletion(task.id, task.isCompleted)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            _recentlyDeletedTask.value = task
            taskUseCases.deleteTask(task)
            if (_editingTask.value?.id == task.id) {
                closeEditor()
            }
        }
    }

    fun undoDelete() {
        val taskToRestore = _recentlyDeletedTask.value ?: return
        viewModelScope.launch {
            taskUseCases.saveTask(taskToRestore)
            _recentlyDeletedTask.value = null
        }
    }

    fun clearRecentlyDeleted() {
        _recentlyDeletedTask.value = null
    }
}
