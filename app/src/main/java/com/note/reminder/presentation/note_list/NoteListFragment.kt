package com.note.reminder.presentation.note_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.note.reminder.R
import com.note.reminder.common.ui.BaseFragment
import com.note.reminder.common.ui.onQueryTextChanged
import com.note.reminder.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : BaseFragment<FragmentNoteListBinding>() {
    private var adapter: NoteListAdapter? = null
    private val viewModel : NoteListViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNoteListBinding.inflate(layoutInflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        observeData()
        observeUiEvent()

        handleSearch()
        handleButtonClicks()
    }
    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteListEventFlow.collect { event ->
                when (event) {
                    is NoteListViewModel.UiNoteListEvent.ShowUndoDeleteNoteMessage -> {
//                        showUndoDeleteNoteMessage(event.note)
                    }
                    is NoteListViewModel.UiNoteListEvent.NavigateToAddEditScreen -> {
                        val action =
                            NoteListFragmentDirections.actionNoteListFragmentToNoteAddFragment()
                        findNavController().navigate(action)
//                        findNavController().navigate(
//                            R.id.action_noteListFragment_to_noteAddFragment
//                        )
                    }
                    is NoteListViewModel.UiNoteListEvent.NavigateToDetailsNoteScreen -> {
                        val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(event.note)
                        findNavController().navigate(action)
                    }
                    is NoteListViewModel.UiNoteListEvent.NavigateToSortDialogScreen -> {
                        findNavController().navigate(R.id.action_noteListFragment_to_noteListBottomSheetFragment)
                    }
                }
            }
        }
    }
    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notes.collect { notes ->
                adapter?.submitList(notes)
            }
        }
    }
    private fun handleSearch() {
        binding.searchNote.onQueryTextChanged { query ->
            viewModel.onEvent(NoteListEvent.QueryEntered(query))
        }
    }
    private fun handleButtonClicks() {
        binding.fabAddNote.setOnClickListener {
            viewModel.onEvent(NoteListEvent.AddNewNoteClicked)
        }
        binding.icSort.setOnClickListener {
            viewModel.onEvent(NoteListEvent.SortButtonClicked)
        }
    }
    private fun setupAdapter() {
        adapter = NoteListAdapter(
            onMoveToDetail = { note ->
                viewModel.onEvent(NoteListEvent.NoteSelected(note))
            }
        )
        binding.rvNoteList.adapter = adapter
        setupItemTouchHelper()
    }
    private fun setupItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter?.currentList?.get(viewHolder.adapterPosition)
                note?.let { viewModel.onEvent(NoteListEvent.NoteSwiped(note)) }
            }
        }).attachToRecyclerView(binding.rvNoteList)
    }

}