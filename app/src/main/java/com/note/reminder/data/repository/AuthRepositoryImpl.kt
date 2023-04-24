package com.note.reminder.data.repository

import com.google.firebase.auth.*
import com.note.reminder.common.settings.UserSessionStorage
import com.note.reminder.common.utils.Resource
import com.note.reminder.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userSessionStorage: UserSessionStorage
) :AuthRepository{
    override fun signUp(email: String, password: String): Flow<Resource<AuthResult>> = flow {
            emit(Resource.Loading())
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(
                    email,password
                ).await()
                userSessionStorage.saveUserSessionId(result.user?.uid ?: "")
                emit(Resource.Success(result))

            }catch (e: FirebaseAuthUserCollisionException){
                emit(Resource.Error(error =  e.message))
            }catch (e: FirebaseAuthWeakPasswordException) {
                emit(Resource.Error(error = e.message))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                emit(Resource.Error(error = e.message))
            } catch (e: Exception) {
                emit(Resource.Error(error = e.message))
            }
    }

    override fun signIn(email: String, password: String): Flow<Resource<AuthResult>> = flow {
            emit(Resource.Loading())

            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                userSessionStorage.saveUserSessionId(result.user?.uid ?: "")
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(error = e.message))
            }
    }

}