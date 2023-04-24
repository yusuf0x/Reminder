package com.note.reminder.domain.use_case.auth

import com.note.reminder.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email:String,password:String) = repository.signIn(email=email,password=password)

}