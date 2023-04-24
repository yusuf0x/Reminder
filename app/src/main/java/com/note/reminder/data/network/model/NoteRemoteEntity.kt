package com.note.reminder.data.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NoteRemoteEntity(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val color: String = "",
    val timestamp: Long = 0L,
) : Parcelable
