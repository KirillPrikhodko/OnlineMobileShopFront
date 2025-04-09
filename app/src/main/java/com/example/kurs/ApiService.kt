package com.example.kurs
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Products
    @GET("products")
    suspend fun getProducts(
        @Query("category") categoryId: Int? = null,
        @Query("search") searchQuery: String? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null
    ): Response<List<Product>>

    @GET("products/{product_id}/reviews")
    suspend fun getProductReviews(
        @Path("product_id") productId: Int
    ): Response<List<Review>>

    // Orders
    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    // Auth
    @POST("register")
    suspend fun registerUser(
        @Body registrationData: RegistrationData
    ): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginData: LoginData
    ): Response<AuthResponse>

    // User
    @GET("user/orders")
    suspend fun getUserOrders(
        @Header("Authorization") token: String
    ): Response<List<Order>>
}