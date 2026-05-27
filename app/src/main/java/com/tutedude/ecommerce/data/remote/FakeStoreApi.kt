package com.tutedude.ecommerce.data.remote

import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<FakeStoreProduct>
}
