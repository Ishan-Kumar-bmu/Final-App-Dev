package com.tutedude.ecommerce.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tutedude.ecommerce.data.local.ProductDao
import com.tutedude.ecommerce.data.local.ProductEntity
import com.tutedude.ecommerce.data.remote.FakeStoreApi
import com.tutedude.ecommerce.domain.models.Product
import com.tutedude.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val fakeStoreApi: FakeStoreApi,
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            val products = snapshot.documents.mapNotNull { doc ->
                val images = doc.get("images") as? List<String> ?: emptyList()
                Product(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    price = doc.getDouble("price") ?: 0.0,
                    images = images,
                    uploaderId = doc.getString("uploaderId") ?: "",
                    uploaderName = doc.getString("uploaderName") ?: "",
                    uploaderContact = doc.getString("uploaderContact") ?: "",
                    isRecommended = false
                )
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecommendedProducts(): Result<List<Product>> {
        return try {
            val remoteProducts = fakeStoreApi.getProducts()
            val products = remoteProducts.map {
                Product(
                    id = it.id.toString(),
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    images = listOf(it.image),
                    uploaderId = "fakestore",
                    uploaderName = "FakeStore API",
                    uploaderContact = "N/A",
                    isRecommended = true
                )
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProduct(
        title: String,
        description: String,
        price: Double,
        images: List<Uri>,
        uploaderId: String,
        uploaderName: String,
        uploaderContact: String
    ): Result<Unit> {
        return try {
            val uploadedImageUrls = withContext(Dispatchers.IO) {
                images.map { uri ->
                    async {
                        val ref = storage.reference.child("product_images/${UUID.randomUUID()}")
                        ref.putFile(uri).await()
                        ref.downloadUrl.await().toString()
                    }
                }.awaitAll()
            }

            val productMap = mapOf(
                "title" to title,
                "description" to description,
                "price" to price,
                "images" to uploadedImageUrls,
                "uploaderId" to uploaderId,
                "uploaderName" to uploaderName,
                "uploaderContact" to uploaderContact
            )

            firestore.collection("products").add(productMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavorites(): Flow<List<Product>> {
        return productDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    price = entity.price,
                    images = listOf(entity.imageUrl),
                    uploaderId = entity.uploaderId,
                    uploaderName = entity.uploaderName,
                    uploaderContact = entity.uploaderContact,
                    isRecommended = entity.isRecommended
                )
            }
        }
    }

    override suspend fun toggleFavorite(product: Product) {
        val isFav = productDao.isFavorite(product.id).first()
        val entity = ProductEntity(
            id = product.id,
            title = product.title,
            description = product.description,
            price = product.price,
            imageUrl = product.images.firstOrNull() ?: "",
            uploaderId = product.uploaderId,
            uploaderName = product.uploaderName,
            uploaderContact = product.uploaderContact,
            isRecommended = product.isRecommended
        )
        if (isFav) {
            productDao.deleteFavorite(entity)
        } else {
            productDao.insertFavorite(entity)
        }
    }

    override fun isFavorite(productId: String): Flow<Boolean> {
        return productDao.isFavorite(productId)
    }
}
