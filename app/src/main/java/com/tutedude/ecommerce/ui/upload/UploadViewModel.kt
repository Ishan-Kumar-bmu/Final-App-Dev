package com.tutedude.ecommerce.ui.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutedude.ecommerce.domain.repository.AuthRepository
import com.tutedude.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    fun uploadProduct(
        title: String,
        description: String,
        price: Double,
        images: List<Uri>,
        contact: String
    ) {
        if (images.size < 3) {
            _uploadState.value = UploadState.Error("Minimum 3 images required")
            return
        }

        viewModelScope.launch {
            _uploadState.value = UploadState.Uploading
            val user = authRepository.currentUser.firstOrNull()
            
            if (user == null) {
                _uploadState.value = UploadState.Error("User not logged in")
                return@launch
            }

            productRepository.uploadProduct(
                title = title,
                description = description,
                price = price,
                images = images,
                uploaderId = user.id,
                uploaderName = user.name,
                uploaderContact = contact
            ).fold(
                onSuccess = { _uploadState.value = UploadState.Success },
                onFailure = { _uploadState.value = UploadState.Error(it.message ?: "Upload failed") }
            )
        }
    }

    fun resetState() {
        _uploadState.value = UploadState.Idle
    }
}
