package com.saddiqueini.slateflow.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddiqueini.slateflow.data.model.NoteEntity
import com.saddiqueini.slateflow.domain.NoteUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    private val _isEditorOpen = MutableStateFlow(false)
    val isEditorOpen: StateFlow<Boolean> = _isEditorOpen.asStateFlow()

    private val _editingNote = MutableStateFlow<NoteEntity?>(null)
    val editingNote: StateFlow<NoteEntity?> = _editingNote.asStateFlow()

    val notes: StateFlow<List<NoteEntity>> = _searchQuery
        .flatMapLatest { query ->
            noteUseCases.getNotes(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun openCreateNote() {
        _editingNote.value = null
        _isEditorOpen.value = true
    }

    fun openEditNote(note: NoteEntity) {
        _editingNote.value = note
        _isEditorOpen.value = true
    }

    fun closeEditor() {
        _isEditorOpen.value = false
        _editingNote.value = null
    }

    fun saveNote(title: String, content: String, isPinned: Boolean, colorIndex: Int) {
        viewModelScope.launch {
            val current = _editingNote.value
            val noteToSave = if (current != null) {
                current.copy(
                    title = title.trim(),
                    content = content.trim(),
                    timestamp = System.currentTimeMillis(),
                    isPinned = isPinned,
                    colorIndex = colorIndex
                )
            } else {
                NoteEntity(
                    title = title.trim(),
                    content = content.trim(),
                    timestamp = System.currentTimeMillis(),
                    isPinned = isPinned,
                    colorIndex = colorIndex
                )
            }
            noteUseCases.saveNote(noteToSave)
            closeEditor()
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            noteUseCases.togglePin(note.id, note.isPinned)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
            if (_editingNote.value?.id == note.id) {
                closeEditor()
            }
        }
    }
}
