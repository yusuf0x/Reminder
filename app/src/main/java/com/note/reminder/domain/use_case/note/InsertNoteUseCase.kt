package com.note.reminder.domain.use_case.note

import com.note.reminder.domain.model.Note
import com.note.reminder.domain.repository.NoteRepository
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(
    private val repository: NoteRepository
){
    suspend operator fun invoke(note: Note) = repository.insertNote(note)
}