package com.note.reminder.presentation.note_add_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.note.reminder.R
import com.note.reminder.common.ui.BaseFragment
import com.note.reminder.databinding.FragmentNoteAddEditBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteAddEditFragment : BaseFragment<FragmentNoteAddEditBinding>() {
    private val viewModel: NoteAddEditViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNoteAddEditBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNoteInfoToUi()
        observeUiEvent()
        listenToNoteChanges()
        handleButtonClicks()

    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteAddEditEvent.collect { event ->
                when (event) {
                    is NoteAddEditViewModel.UiAddEditEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.message.asString(requireActivity()),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is NoteAddEditViewModel.UiAddEditEvent.NavigateToListScreen -> {
                        findNavController().navigate(R.id.action_noteAddFragment_to_noteListFragment)
                    }
                    is NoteAddEditViewModel.UiAddEditEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun listenToNoteChanges() {
        binding.edTitle.addTextChangedListener {
            viewModel.onEvent(NoteAddEditEvent.TitleChanged(it.toString()))
        }
        binding.edContent.addTextChangedListener {
            viewModel.onEvent(NoteAddEditEvent.ContentChanged(it.toString()))
        }
    }


    private fun handleButtonClicks() = binding.apply {
        btnSaveNote.setOnClickListener {
            viewModel.onEvent(NoteAddEditEvent.NoteSubmitted)
            Toast.makeText(requireActivity().applicationContext, "clicked", Toast.LENGTH_SHORT).show()

        }
        btnGoBack.setOnClickListener {
            viewModel.onEvent(NoteAddEditEvent.BackButtonClicked)
        }
    }

    private fun setNoteInfoToUi() {
        binding.edTitle.setText(viewModel.noteTitle)
        binding.edContent.setText(viewModel.noteContent)
    }
}