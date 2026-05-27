package com.tutedude.ecommerce.domain.models

data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val images: List<String> = emptyList(),
    val uploaderId: String = "",
    val uploaderName: String = "",
    val uploaderContact: String = "",
    val isRecommended: Boolean = false // Flag to distinguish FakeStore products
)
