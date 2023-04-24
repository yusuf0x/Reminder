package com.note.reminder.presentation.node_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.note.reminder.common.utils.Constants
import com.note.reminder.common.utils.StateStringPropertyDelegate
import com.note.reminder.domain.model.Note
import com.note.reminder.domain.use_case.note.DeleteNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    state: SavedStateHandle
) : ViewModel() {

    private val note = state.get<Note>("note")

    var noteTitle by StateStringPropertyDelegate(
        state = state,
        key = "title",
        initialValue = Constants.EMPTY_VALUE,
        savedValue = note?.title
    )
    var noteContent by StateStringPropertyDelegate(
        state = state,
        key = "content",
        initialValue = Constants.EMPTY_VALUE,
        savedValue = note?.content,
    )

    private val _noteDetailsChannel = Channel<UiNoteDetailsEvent>()
    val noteDetailsEvent get() = _noteDetailsChannel.receiveAsFlow()

    fun onEvent(noteDetailEvent: NoteDetailEvent) {
        when (noteDetailEvent) {
            is NoteDetailEvent.NoteDeleted -> deleteNote()
            is NoteDetailEvent.EditButtonClicked -> navigateToAddEditScreen()
            is NoteDetailEvent.BackButtonClicked -> navigateBack()
        }
    }

    private fun navigateToAddEditScreen() = viewModelScope.launch {
        note?.let { note ->
            _noteDetailsChannel.send(UiNoteDetailsEvent.NavigateToAddEditScreen(note))
        }
    }

    private fun navigateBack() = viewModelScope.launch {
        _noteDetailsChannel.send(UiNoteDetailsEvent.NavigateBack)
    }

    private fun deleteNote() = viewModelScope.launch {
        note?.let { note ->
            deleteNoteUseCase(note)
            Log.d("DELETE","DELETED")
            _noteDetailsChannel.send(UiNoteDetailsEvent.NavigateToListScreen)
        }
    }

    sealed class UiNoteDetailsEvent {
        data class NavigateToAddEditScreen(val note: Note) : UiNoteDetailsEvent()
        object NavigateBack : UiNoteDetailsEvent()
        object NavigateToListScreen : UiNoteDetailsEvent()
    }
}