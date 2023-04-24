package com.note.reminder.data.repository

import android.util.Log
import com.note.reminder.data.mapper.toNoteRemoteEntity
import com.note.reminder.data.network.NoteFirestore
import com.note.reminder.domain.model.Note
import com.note.reminder.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api : NoteFirestore
) :NoteRepository {
    override suspend fun synchronizeNotes() {
        try {
            val remoteNotes = api.getAllNotes()
            remoteNotes.onEach { remoteNote ->
            }

        } catch (e: Exception) {
            Log.d("RepositoryException", e.localizedMessage ?: "Error")
        }
    }

    override suspend fun restoreDeletedNote(note: Note) {
        try {
            val remoteEntity = note.toNoteRemoteEntity()
            api.insertNote(remoteEntity)
            api.deleteDeletedNote(remoteEntity)
        } catch (e: Exception) {
            Log.d("RepositoryException", e.localizedMessage ?: "Error")
        }
    }

    override fun getFilteredNotes(
        query: String,
        sortType: NoteRepository.SortType
    ): Flow<List<Note>> = flow {
        val data = api.getAllNotes()
        val new = data.map {
            Note(
                title =  it.title,
                id =  it.id,
                timestamp =  it.timestamp,
                color = it.color,
                content = it.content
            )
        }
        emit(new)
    }

    override suspend fun insertNote(note: Note) {
        try {
            val remoteEntity = note.toNoteRemoteEntity()
            api.insertNote(remoteEntity)
        } catch (e: Exception) {
            Log.d("RepositoryException", e.localizedMessage ?: "Error")
        }
    }

    override suspend fun deleteNote(note: Note) {
        try {

            val remoteEntity = note.toNoteRemoteEntity()
            api.deleteNote(remoteEntity)
//            api.insertDeletedNote(remoteEntity)

        } catch (e: Exception) {
            Log.d("RepositoryException", e.localizedMessage ?: "Error")
        }
    }

    override suspend fun updateNote(note: Note) {
        try {
            val remoteEntity = note.toNoteRemoteEntity()
            api.updateNote(remoteEntity)
        } catch (e: Exception) {
            Log.d("RepositoryException", e.localizedMessage ?: "Error")
        }
    }
}