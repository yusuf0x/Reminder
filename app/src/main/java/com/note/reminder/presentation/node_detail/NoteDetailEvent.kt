package com.note.reminder.presentation.node_detail

sealed class NoteDetailEvent {
    object EditButtonClicked : NoteDetailEvent()
    object BackButtonClicked : NoteDetailEvent()
    object NoteDeleted : NoteDetailEvent()
}