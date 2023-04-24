package com.note.reminder.domain.use_case.auth

import com.note.reminder.common.ui.UiText
import com.note.reminder.common.utils.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase  @Inject constructor( ) {
    operator fun invoke(email:String) : ValidationResult{
        if(email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString("The email can't be blank")

            )
        }
        return ValidationResult(
            successful = true
        )
    }
}