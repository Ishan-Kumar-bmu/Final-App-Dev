package com.tutedude.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(product: ProductEntity)

    @Delete
    suspend fun deleteFavorite(product: ProductEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>
}
