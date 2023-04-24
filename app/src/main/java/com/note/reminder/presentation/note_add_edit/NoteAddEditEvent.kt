package com.note.reminder.presentation.note_add_edit

sealed class NoteAddEditEvent {
    object NoteSubmitted : NoteAddEditEvent()
    object BackButtonClicked : NoteAddEditEvent()
    data class TitleChanged(val title: String): NoteAddEditEvent()
    data class ContentChanged(val content: String): NoteAddEditEvent()
}