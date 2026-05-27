package com.tutedude.ecommerce.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutedude.ecommerce.domain.models.Product
import com.tutedude.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            productRepository.isFavorite(productId).collect {
                _isFavorite.value = it
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            productRepository.toggleFavorite(product)
        }
    }
}
