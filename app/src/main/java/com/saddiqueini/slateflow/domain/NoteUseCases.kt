package com.saddiqueini.slateflow.domain

import com.saddiqueini.slateflow.data.model.NoteEntity
import com.saddiqueini.slateflow.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteUseCases(
    private val repository: NoteRepository
) {
    fun getNotes(query: String = ""): Flow<List<NoteEntity>> {
        return if (query.isBlank()) {
            repository.getAllNotes()
        } else {
            repository.searchNotes(query.trim())
        }
    }

    suspend fun saveNote(note: NoteEntity): Long {
        return if (note.id == 0L) {
            repository.insertNote(note)
        } else {
            repository.updateNote(note)
            note.id
        }
    }

    suspend fun deleteNote(note: NoteEntity) {
        repository.deleteNote(note)
    }

    suspend fun togglePin(id: Long, currentPinStatus: Boolean) {
        repository.togglePin(id, currentPinStatus)
    }
}
