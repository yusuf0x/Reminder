package com.note.reminder.domain.use_case.auth

import com.note.reminder.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email:String,password:String) = repository.signUp(email=email,password=password)
    
}