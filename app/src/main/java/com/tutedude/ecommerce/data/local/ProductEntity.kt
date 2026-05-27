package com.tutedude.ecommerce.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String, // Store first image for thumbnail
    val uploaderId: String,
    val uploaderName: String,
    val uploaderContact: String,
    val isRecommended: Boolean
)
