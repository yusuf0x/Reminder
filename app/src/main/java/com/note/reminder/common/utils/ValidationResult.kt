package com.note.reminder.common.utils

import com.note.reminder.common.ui.UiText

data class ValidationResult(
    val successful: Boolean,
    var errorMessage: UiText? = null
)