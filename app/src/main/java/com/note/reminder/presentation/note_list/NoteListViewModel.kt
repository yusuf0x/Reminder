package com.note.reminder.presentation.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.note.reminder.common.utils.Constants
import com.note.reminder.domain.model.Note
import com.note.reminder.domain.repository.NoteRepository
import com.note.reminder.domain.use_case.note.DeleteNoteUseCase
import com.note.reminder.domain.use_case.note.GetFilteredNotesUseCase
import com.note.reminder.domain.use_case.note.RestoreDeletedNoteUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getFilteredNotesUseCase: GetFilteredNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val restoreDeletedNoteUseCase: RestoreDeletedNoteUseCase,
//    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle
) : ViewModel() {

    private val searchQuery = state.getLiveData("searchQuery", Constants.EMPTY_VALUE)
//    val preferencesFlow = preferencesManager.getUserSettings()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notesFlow = combine(
        searchQuery.asFlow(),
//        preferencesFlow
    ) { query ->
        query
    }.flatMapLatest { (searchQuery) ->
        getFilteredNotesUseCase(searchQuery, NoteRepository.SortType.ASCENDING)
    }

    val notes = notesFlow.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _noteListEventChannel = Channel<UiNoteListEvent>()
    val noteListEventFlow get() = _noteListEventChannel.receiveAsFlow()

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.QueryEntered -> onQueryChanged(event.query)
            is NoteListEvent.SortOrderSelected -> saveSortType(event.sortType)
            is NoteListEvent.NoteSwiped -> swipeToDeleteNote(event.note)
            is NoteListEvent.DeletedNoteRestored -> restoreDeletedNote(event.note)
            is NoteListEvent.AddNewNoteClicked -> navigateToAddEditScreen()
            is NoteListEvent.NoteSelected -> navigateToDetailsNoteScreen(event.note)
            is NoteListEvent.SortButtonClicked -> navigateToSortDialogScreen()
        }
    }

    private fun onQueryChanged(query: String) {
        searchQuery.value = query
    }

    private fun saveSortType(sortType: NoteRepository.SortType) = viewModelScope.launch {
//        preferencesManager.saveSortType(sortType)
    }

    private fun swipeToDeleteNote(note: Note) = viewModelScope.launch {
        deleteNoteUseCase(note)
        _noteListEventChannel.send(UiNoteListEvent.ShowUndoDeleteNoteMessage(note))
    }

    private fun restoreDeletedNote(note: Note) = viewModelScope.launch {
        restoreDeletedNoteUseCase(note)
    }

    private fun navigateToAddEditScreen() = viewModelScope.launch {
        _noteListEventChannel.send(UiNoteListEvent.NavigateToAddEditScreen)
    }

    private fun navigateToSortDialogScreen() = viewModelScope.launch {
        _noteListEventChannel.send(UiNoteListEvent.NavigateToSortDialogScreen)
    }

    private fun navigateToDetailsNoteScreen(note: Note) = viewModelScope.launch {
        _noteListEventChannel.send(UiNoteListEvent.NavigateToDetailsNoteScreen(note))
    }

    sealed class UiNoteListEvent {
        data class ShowUndoDeleteNoteMessage(val note: Note) : UiNoteListEvent()
        data class NavigateToDetailsNoteScreen(val note: Note) : UiNoteListEvent()
        object NavigateToAddEditScreen : UiNoteListEvent()
        object NavigateToSortDialogScreen : UiNoteListEvent()
    }
}
