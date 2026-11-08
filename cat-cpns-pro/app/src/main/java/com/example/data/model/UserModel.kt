package com.example.data.model

enum class UserRole(val displayName: String) {
    USER("Peserta CPNS"),
    ADMIN("Administrator CAT")
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val participantNumber: String,
    val institution: String,
    val targetFormation: String
)

data class AuthState(
    val currentUser: UserProfile? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)
