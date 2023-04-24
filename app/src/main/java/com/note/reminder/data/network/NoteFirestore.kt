package com.note.reminder.data.network

import com.note.reminder.data.network.model.NoteRemoteEntity


interface NoteFirestore {

    suspend fun getAllNotes(): List<NoteRemoteEntity>

    suspend fun insertNote(noteRemoteEntity: NoteRemoteEntity)

    suspend fun deleteNote(noteRemoteEntity: NoteRemoteEntity)

    suspend fun updateNote(noteRemoteEntity: NoteRemoteEntity)

    suspend fun insertDeletedNote(noteRemoteEntity: NoteRemoteEntity)

    suspend fun deleteDeletedNote(noteRemoteEntity: NoteRemoteEntity)

    suspend fun getDeletedNotes(): List<NoteRemoteEntity>
}