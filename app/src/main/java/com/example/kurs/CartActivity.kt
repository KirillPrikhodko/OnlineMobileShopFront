package com.example.kurs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurs.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        updateCartViews()
        setupCheckoutButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Корзина"
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter {
            updateCartViews()
        }

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter

        loadCartItems()
    }

    private fun loadCartItems() {
        val cartItems = CartManager.getCartItemsWithProducts()
        adapter.submitList(cartItems)
        updateCartViews()
    }

    private fun updateCartViews() {
        val totalPrice = CartManager.getTotalPrice()
        val itemCount = CartManager.getTotalItemsCount()

        binding.totalPrice.text = "Итого: %.2f ₽".format(totalPrice)
        binding.itemCount.text = "$itemCount товаров"

        if (itemCount == 0) {
            binding.emptyCartView.visibility = View.VISIBLE
            binding.cartContent.visibility = View.GONE
            binding.checkoutButton.isEnabled = false
        } else {
            binding.emptyCartView.visibility = View.GONE
            binding.cartContent.visibility = View.VISIBLE
            binding.checkoutButton.isEnabled = true
        }
    }

    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            // Оформление заказа
            showCheckoutDialog()
        }
    }

    private fun showCheckoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Оформление заказа")
            .setMessage("Подтвердите оформление заказа на сумму ${CartManager.getTotalPrice()} ₽")
            .setPositiveButton("Подтвердить") { _, _ ->
                CartManager.clearCart()
                Toast.makeText(this, "Заказ успешно оформлен!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
