package com.note.reminder.domain.use_case.auth

import com.note.reminder.common.ui.UiText
import com.note.reminder.common.utils.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor( ) {

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString("The password can't be blank")
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}