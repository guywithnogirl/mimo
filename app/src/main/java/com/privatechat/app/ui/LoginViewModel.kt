package com.privatechat.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privatechat.app.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginState(val loading: Boolean = false, val authenticated: Boolean = false, val error: String? = null)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(LoginState())
    val state = mutableState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            mutableState.value = LoginState(error = "Enter your username and password.")
            return
        }
        viewModelScope.launch {
            mutableState.value = LoginState(loading = true)
            try {
                repository.login(username.trim(), password)
                mutableState.value = LoginState(authenticated = true)
            } catch (_: Exception) {
                mutableState.value = LoginState(error = "Sign in failed. Check your details and connection.")
            }
        }
    }
}
