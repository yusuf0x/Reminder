package com.note.reminder.presentation.note_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.note.reminder.databinding.FragmentNoteListBottomSheetBinding
import com.note.reminder.domain.repository.NoteRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentNoteListBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NoteListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSortEvent()

        observeUi()
    }

    private fun observeUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            when (viewModel..first().sortType) {
//                NoteRepository.SortType.ASCENDING -> binding.rbSortAscending.isChecked = true
//                NoteRepository.SortType.DESCENDING -> binding.rbSortDescending.isChecked = true
//            }
        }
    }

    private fun handleSortEvent() {
        binding.apply {
            rbSortAscending.setOnClickListener {
                viewModel.onEvent(NoteListEvent.SortOrderSelected(NoteRepository.SortType.ASCENDING))
            }
            rbSortDescending.setOnClickListener {
                viewModel.onEvent(NoteListEvent.SortOrderSelected(NoteRepository.SortType.DESCENDING))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}