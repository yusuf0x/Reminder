package com.note.reminder.presentation.sign_in

sealed class SignInEvent {
    data class EmailChanged(val email: String): SignInEvent()
    data class PasswordChanged(val password: String): SignInEvent()
    object LoginButtonClicked: SignInEvent()
    object RegisterNewAccountButtonClicked : SignInEvent()
}