package com.note.reminder.presentation.note_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.note.reminder.databinding.NoteItemBinding
import com.note.reminder.domain.model.Note


class NoteListAdapter(
    private var onMoveToDetail: (Note) -> Unit,
) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback) {

    class NoteViewHolder(
        private val binding: NoteItemBinding,
        private var onMoveToDetail: (Note) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.apply {
                tvDate.text = note.createdDateFormatted
                cvNoteItem.setOnClickListener { onMoveToDetail(note) }
                cvNoteItem.setCardBackgroundColor(Color.parseColor(note.color))
                tvTitle.text = note.title
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(
            NoteItemBinding.inflate(layoutInflater, parent, false),
            onMoveToDetail
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.timestamp == oldItem.timestamp
        }
    }
}