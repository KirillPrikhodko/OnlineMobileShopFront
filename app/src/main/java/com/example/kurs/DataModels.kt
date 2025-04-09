package com.example.kurs

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("stock") val stockQuantity: Int
)

data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String
)

data class OrderRequest(
    val items: List<OrderItem>,
    val shippingAddress: String,
    val paymentMethod: String
)

data class OrderItem(
    val productId: Int,
    val quantity: Int
)

data class OrderResponse(
    val orderId: Int,
    val totalAmount: Double,
    val status: String
)

data class RegistrationData(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?
)

data class LoginData(
    val email: String,
    val password: String
)

data class AuthResponse(
    val userId: Int,
    val token: String
)

data class Order(
    val orderId: Int,
    val date: String,
    val total: Double,
    val status: String
)