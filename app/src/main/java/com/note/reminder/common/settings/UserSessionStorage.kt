package com.note.reminder.common.settings

interface UserSessionStorage {

    fun saveUserSessionId(userId: String)

    fun getUserSessionId(): String
}