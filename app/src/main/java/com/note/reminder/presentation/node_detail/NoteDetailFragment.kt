package com.note.reminder.presentation.node_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.note.reminder.R
import com.note.reminder.common.ui.BaseFragment
import com.note.reminder.databinding.FragmentNoteDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : BaseFragment<FragmentNoteDetailBinding>() {
    val args: NoteDetailFragmentArgs by navArgs()
    private val viewModel: NoteDetailViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNoteInfoToUi()

        observeUiEvents()

        handleButtonClicks()
    }
    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteDetailsEvent.collect { event ->
                when (event) {
                    is NoteDetailViewModel.UiNoteDetailsEvent.NavigateToAddEditScreen -> {
                        val action =
                            NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteAddFragment(
                                event.note
                            )
                        findNavController().navigate(action)
                    }
                    is NoteDetailViewModel.UiNoteDetailsEvent.NavigateToListScreen -> {
                        findNavController().navigate(R.id.action_noteDetailFragment_to_noteListFragment)
                    }
                    is NoteDetailViewModel.UiNoteDetailsEvent.NavigateBack -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun handleButtonClicks() = binding.apply {
        btnEdit.setOnClickListener {
            viewModel.onEvent(NoteDetailEvent.EditButtonClicked)
        }
        btnDelete.setOnClickListener {
            viewModel.onEvent(NoteDetailEvent.NoteDeleted)
        }
        btnGoBack.setOnClickListener {
            viewModel.onEvent(NoteDetailEvent.BackButtonClicked)
        }
    }

    private fun setNoteInfoToUi() {
        binding.apply {
            tvTitle.text = viewModel.noteTitle
            tvContent.text = viewModel.noteContent
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNoteDetailBinding.inflate(inflater, container, false)

}