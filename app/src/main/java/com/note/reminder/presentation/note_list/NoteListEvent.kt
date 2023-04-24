package com.note.reminder.presentation.note_list


import com.note.reminder.domain.model.Note
import com.note.reminder.domain.repository.NoteRepository


sealed class NoteListEvent {
    data class QueryEntered(val query: String) : NoteListEvent()
    data class SortOrderSelected(val sortType: NoteRepository.SortType) : NoteListEvent()
    data class NoteSwiped(val note: Note) : NoteListEvent()
    data class DeletedNoteRestored(val note: Note) : NoteListEvent()
    data class NoteSelected(val note: Note) : NoteListEvent()
    object AddNewNoteClicked : NoteListEvent()
    object SortButtonClicked : NoteListEvent()
}