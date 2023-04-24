package com.note.reminder.domain.repository

import com.google.firebase.auth.AuthResult
import com.note.reminder.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(email:String,password:String) : Flow<Resource<AuthResult>>
    fun signIn(email:String,password:String) : Flow<Resource<AuthResult>>
}