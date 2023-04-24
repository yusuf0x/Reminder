package com.note.reminder.domain.use_case.note

import com.note.reminder.R
import com.note.reminder.common.ui.UiText
import com.note.reminder.common.utils.ValidationResult
import javax.inject.Inject

class ValidateTitleUseCase @Inject constructor() {

    operator fun invoke(title: String): ValidationResult {
        if (title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString(
                    "The title can't be blank"
                )
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}