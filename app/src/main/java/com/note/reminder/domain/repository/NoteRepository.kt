package com.note.reminder.domain.repository


import com.note.reminder.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun synchronizeNotes()

    suspend fun restoreDeletedNote(note: Note)

    fun getFilteredNotes(query: String, sortType: SortType): Flow<List<Note>>

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    enum class SortType {
        ASCENDING, DESCENDING
    }
}