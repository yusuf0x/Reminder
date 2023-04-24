package com.note.reminder.hilt

import com.google.firebase.auth.FirebaseAuth
import com.note.reminder.NoteApp
import com.note.reminder.common.settings.UserSessionStorage
import com.note.reminder.common.settings.UserSessionStorageImpl
import com.note.reminder.data.network.NoteFirestore
import com.note.reminder.data.network.NoteFirestoreImpl
import com.note.reminder.data.repository.AuthRepositoryImpl
import com.note.reminder.data.repository.NoteRepositoryImpl
import com.note.reminder.domain.repository.AuthRepository
import com.note.reminder.domain.repository.NoteRepository
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth,userSessionStorage: UserSessionStorage): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth,userSessionStorage)
    }
    @Provides
    @Singleton
    fun provideNoteRepository(noteFirestore: NoteFirestore): NoteRepository {
        return NoteRepositoryImpl(noteFirestore)
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideUserSessionStorage() : UserSessionStorage {
        return UserSessionStorageImpl(context = NoteApp.context)
    }
    @Provides
    @Singleton
    fun provideNoteFirestore(userSessionStorage: UserSessionStorage) : NoteFirestore {
        return NoteFirestoreImpl(userSessionStorage)
    }
}