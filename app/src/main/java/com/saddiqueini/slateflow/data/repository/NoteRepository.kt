package com.saddiqueini.slateflow.data.repository

import com.saddiqueini.slateflow.data.local.NoteDao
import com.saddiqueini.slateflow.data.model.NoteEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun searchNotes(query: String): Flow<List<NoteEntity>>
    suspend fun getNoteById(id: Long): NoteEntity?
    suspend fun insertNote(note: NoteEntity): Long
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    suspend fun togglePin(id: Long, currentPinStatus: Boolean)
}

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> =
        noteDao.getAllNotes().flowOn(ioDispatcher)

    override fun searchNotes(query: String): Flow<List<NoteEntity>> =
        noteDao.searchNotes(query).flowOn(ioDispatcher)

    override suspend fun getNoteById(id: Long): NoteEntity? =
        withContext(ioDispatcher) {
            noteDao.getNoteById(id)
        }

    override suspend fun insertNote(note: NoteEntity): Long =
        withContext(ioDispatcher) {
            noteDao.insertNote(note)
        }

    override suspend fun updateNote(note: NoteEntity) =
        withContext(ioDispatcher) {
            noteDao.updateNote(note)
        }

    override suspend fun deleteNote(note: NoteEntity) =
        withContext(ioDispatcher) {
            noteDao.deleteNote(note)
        }

    override suspend fun togglePin(id: Long, currentPinStatus: Boolean) =
        withContext(ioDispatcher) {
            noteDao.updatePinStatus(id, !currentPinStatus)
        }
}
