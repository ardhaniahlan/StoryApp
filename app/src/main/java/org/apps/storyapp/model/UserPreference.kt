package org.apps.storyapp.model

import android.content.Context

//Internal
class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "login_pref"
        private const val NAME = "name"
        private const val USER_ID = "userId"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLogin(user: User) {
        val editor = preferences.edit()
        editor.putString(NAME, user.name)
        editor.putString(USER_ID, user.userId)
        editor.putString(TOKEN, user.token)
        editor.apply()
    }

    fun getUser(): User {
        val name = preferences.getString(NAME, null)
        val userId = preferences.getString(USER_ID, null)
        val token = preferences.getString(TOKEN, null)
        return User(userId, name, token)
    }

    fun setLogout() {
        val editor = preferences.edit().clear()
        editor.apply()
    }


}