package com.tutedude.ecommerce.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FakeStoreProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)
