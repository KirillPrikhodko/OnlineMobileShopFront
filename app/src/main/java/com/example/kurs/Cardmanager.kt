package com.example.kurs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    private const val PREFS_NAME = "cart_prefs"
    private const val KEY_CART_ITEMS = "cart_items"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()
    private val cartItemType = object : TypeToken<List<CartItem>>() {}.type

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    fun increaseQuantity(productId: Int) {
        val items = getCartItems()
        items.find { it.productId == productId }?.let {
            it.quantity += 1
            saveCartItems(items)
        }
    }

    fun decreaseQuantity(productId: Int) {
        val items = getCartItems()
        items.find { it.productId == productId }?.let {
            if (it.quantity > 1) {
                it.quantity -= 1
                saveCartItems(items)
            } else {
                removeFromCart(productId)
            }
        }
    }

    fun getItemCount(productId: Int): Int {
        return getCartItems().find { it.productId == productId }?.quantity ?: 0
    }
    fun createOrderRequest(
        shippingAddress: String,
        paymentMethod: String
    ): OrderRequest {
        val cartItems = getCartItems()
        val orderItems = cartItems.map { cartItem ->
            OrderItem(
                productId = cartItem.productId,
                quantity = cartItem.quantity
            )
        }

        return OrderRequest(
            items = orderItems,
            shippingAddress = shippingAddress,
            paymentMethod = paymentMethod
        )
    }
    private fun getCartItems(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        return if (json != null) {
            gson.fromJson(json, cartItemType) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    private fun saveCartItems(items: List<CartItem>) {
        prefs.edit().putString(KEY_CART_ITEMS, gson.toJson(items)).apply()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val items = getCartItems()
        val existingItem = items.find { it.productId == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(
                productId = product.id,
                quantity = quantity,
                price = product.price,
                name = product.name,
                imageUrl = product.imageUrl
            ))
        }
        saveCartItems(items)
    }

    fun removeFromCart(productId: Int) {
        val items = getCartItems()
        items.removeAll { it.productId == productId }
        saveCartItems(items)
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val items = getCartItems()
        items.find { it.productId == productId }?.let {
            it.quantity = newQuantity
            saveCartItems(items)
        }
    }

    fun getCartItemsWithProducts(): List<CartItem> {
        return getCartItems()
    }

    fun clearCart() {
        saveCartItems(emptyList())
    }

    fun getTotalItemsCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }

    fun getTotalPrice(): Double {
        return getCartItems().sumOf { it.price * it.quantity }
    }
}

data class CartItem(
    val productId: Int,
    var quantity: Int,
    val price: Double,
    val name: String,
    val imageUrl: String
)