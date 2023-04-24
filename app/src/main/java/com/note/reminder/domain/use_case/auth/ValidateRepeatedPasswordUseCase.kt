package com.note.reminder.domain.use_case.auth

import android.util.Log
import com.note.reminder.common.ui.UiText
import com.note.reminder.common.utils.ValidationResult
import javax.inject.Inject

class ValidateRepeatedPasswordUseCase @Inject constructor( ) {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if (repeatedPassword != password) {
            Log.d("PASSWORDs",repeatedPassword+"|"+password+"|")
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString("The passwords doesn't match")
            )
        }

        return ValidationResult(successful = true)
    }
}