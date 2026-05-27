package com.tutedude.ecommerce.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutedude.ecommerce.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser = authRepository.currentUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, pass: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        authRepository.login(email, pass).fold(
            onSuccess = { _authState.value = AuthState.Success },
            onFailure = { _authState.value = AuthState.Error(it.message ?: "Login failed") }
        )
    }

    fun register(email: String, pass: String, name: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        authRepository.register(email, pass, name).fold(
            onSuccess = { _authState.value = AuthState.Success },
            onFailure = { _authState.value = AuthState.Error(it.message ?: "Registration failed") }
        )
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
