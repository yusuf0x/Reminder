package com.note.reminder.domain.use_case.note

import com.note.reminder.domain.repository.NoteRepository
import javax.inject.Inject

class GetFilteredNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(
        query: String,
        sortType: NoteRepository.SortType
    ) = repository.getFilteredNotes(query, sortType)
}