package com.tutedude.ecommerce.domain.repository

import android.net.Uri
import com.tutedude.ecommerce.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getRecommendedProducts(): Result<List<Product>>
    suspend fun uploadProduct(
        title: String,
        description: String,
        price: Double,
        images: List<Uri>,
        uploaderId: String,
        uploaderName: String,
        uploaderContact: String
    ): Result<Unit>
    fun getFavorites(): Flow<List<Product>>
    suspend fun toggleFavorite(product: Product)
    fun isFavorite(productId: String): Flow<Boolean>
}
