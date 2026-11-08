package com.example.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private const val PREF_NAME = "cat_cpns_session"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"

    private var sharedPreferences: SharedPreferences? = null

    private val _isLoggedInState = MutableStateFlow(false)
    val isLoggedInState: StateFlow<Boolean> = _isLoggedInState.asStateFlow()

    private val _currentUserState = MutableStateFlow<UserProfile?>(null)
    val currentUserState: StateFlow<UserProfile?> = _currentUserState.asStateFlow()

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val token = getToken()
        if (!token.isNullOrEmpty()) {
            val user = loadUserFromPrefs()
            _currentUserState.value = user
            _isLoggedInState.value = user != null
        }
    }

    fun saveSession(token: String, userDto: UserDto) {
        sharedPreferences?.edit()?.apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userDto.id)
            putString(KEY_USER_NAME, userDto.name)
            putString(KEY_USER_EMAIL, userDto.email)
            putString(KEY_USER_ROLE, userDto.role)
            apply()
        }

        val role = if (userDto.role.equals("ADMIN", ignoreCase = true)) UserRole.ADMIN else UserRole.USER
        val profile = UserProfile(
            id = userDto.id,
            name = userDto.name,
            email = userDto.email,
            role = role,
            participantNumber = "CAT-${userDto.id.takeLast(6).uppercase()}",
            institution = "Kementerian / Lembaga Pusat & Daerah",
            targetFormation = "Analis Kebijakan / Ahli Pertama"
        )
        _currentUserState.value = profile
        _isLoggedInState.value = true
    }

    fun getToken(): String? {
        return sharedPreferences?.getString(KEY_TOKEN, null)
    }

    fun getBearerToken(): String? {
        val token = getToken()
        return if (!token.isNullOrEmpty()) "Bearer $token" else null
    }

    fun clearSession() {
        sharedPreferences?.edit()?.clear()?.apply()
        _currentUserState.value = null
        _isLoggedInState.value = false
    }

    private fun loadUserFromPrefs(): UserProfile? {
        val id = sharedPreferences?.getString(KEY_USER_ID, null) ?: return null
        val name = sharedPreferences?.getString(KEY_USER_NAME, "Peserta") ?: "Peserta"
        val email = sharedPreferences?.getString(KEY_USER_EMAIL, "") ?: ""
        val roleStr = sharedPreferences?.getString(KEY_USER_ROLE, "PARTICIPANT") ?: "PARTICIPANT"
        val role = if (roleStr.equals("ADMIN", ignoreCase = true)) UserRole.ADMIN else UserRole.USER

        return UserProfile(
            id = id,
            name = name,
            email = email,
            role = role,
            participantNumber = "CAT-${id.takeLast(6).uppercase()}",
            institution = "Kementerian / Lembaga Pusat & Daerah",
            targetFormation = "Analis Kebijakan / Ahli Pertama"
        )
    }
}
