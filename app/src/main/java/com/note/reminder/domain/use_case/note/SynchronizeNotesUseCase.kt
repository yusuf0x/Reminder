package com.note.reminder.domain.use_case.note

import com.note.reminder.domain.repository.NoteRepository
import javax.inject.Inject


class SynchronizeNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke() = repository.synchronizeNotes()

}