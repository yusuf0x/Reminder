package com.note.reminder.data.mapper

import com.note.reminder.data.network.model.NoteRemoteEntity
import com.note.reminder.domain.model.Note


fun Note.toNoteRemoteEntity(): NoteRemoteEntity {
    return NoteRemoteEntity(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = color
    )
}