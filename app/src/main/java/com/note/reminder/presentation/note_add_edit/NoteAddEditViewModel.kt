package com.note.reminder.presentation.note_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.reminder.common.ui.UiText
import com.note.reminder.common.utils.Constants
import com.note.reminder.common.utils.StateStringPropertyDelegate
import com.note.reminder.domain.model.Note
import com.note.reminder.domain.use_case.note.InsertNoteUseCase
import com.note.reminder.domain.use_case.note.UpdateNoteUseCase
import com.note.reminder.domain.use_case.note.ValidateTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteAddEditViewModel @Inject constructor(
    private val insertNoteUseCase: InsertNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val validateTitleUseCase: ValidateTitleUseCase,
    state: SavedStateHandle
):ViewModel() {

    private val note = state.get<Note>("note")
    var noteTitle by StateStringPropertyDelegate(
        state = state,
        key = "title",
        initialValue = Constants.EMPTY_VALUE,
        savedValue = note?.title,
    )
    var noteContent by StateStringPropertyDelegate(
        state = state,
        key = "content",
        initialValue = Constants.EMPTY_VALUE,
        savedValue = note?.content
    )
    private var noteColor by StateStringPropertyDelegate(
        state = state,
        key = "color",
        initialValue = Note.colors.random(),
        savedValue = note?.color,
    )
    private var noteId by StateStringPropertyDelegate(
        state = state,
        key = "id",
        initialValue = UUID.randomUUID().toString(),
        savedValue = note?.id,
    )

    private val _noteAddEditChannel = Channel<UiAddEditEvent>()
    val noteAddEditEvent get() = _noteAddEditChannel.receiveAsFlow()

    fun onEvent(event: NoteAddEditEvent) {
        when (event) {
            is NoteAddEditEvent.TitleChanged -> noteTitle = event.title
            is NoteAddEditEvent.ContentChanged -> noteContent = event.content
            is NoteAddEditEvent.BackButtonClicked -> navigateBack()
            is NoteAddEditEvent.NoteSubmitted -> submitNote()
        }
    }

    private fun submitNote() {
        if (!isValidationSuccessful()) return

        if (note != null) {
            val updatedNote = note.copy(
                title = noteTitle,
                content = noteContent,
            )
            updateNote(updatedNote)

        } else {
            val newNote = Note(
                id = noteId,
                title = noteTitle,
                content = noteContent,
                color = noteColor
            )
            insertNote(newNote)
        }
    }

    private fun isValidationSuccessful(): Boolean {
        val titleResult = validateTitleUseCase(noteTitle)
        if (!titleResult.successful) {
            showSnackbar(titleResult.errorMessage!!) // TODO
            return false
        }

        return true
    }

    private fun navigateBack() = viewModelScope.launch {
        _noteAddEditChannel.send(UiAddEditEvent.NavigateBack)
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        updateNoteUseCase(note)
        _noteAddEditChannel.send(UiAddEditEvent.NavigateToListScreen)
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        insertNoteUseCase(note)
        _noteAddEditChannel.send(UiAddEditEvent.NavigateToListScreen)
    }

    private fun showSnackbar(message: UiText) = viewModelScope.launch {
        _noteAddEditChannel.send(UiAddEditEvent.ShowSnackbar(message = message))
    }

    sealed class UiAddEditEvent {
        data class ShowSnackbar(val message: UiText) : UiAddEditEvent()
        object NavigateToListScreen : UiAddEditEvent()
        object NavigateBack : UiAddEditEvent()
    }
}