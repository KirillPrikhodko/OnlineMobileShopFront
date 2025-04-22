package com.example.kurs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.kurs.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var productsAdapter: ProductsAdapter
    private val apiService = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CartManager.init(applicationContext)

        setupRecyclerView()
        loadProducts()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadProducts()
        }
        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        // Раскомментируйте и измените в setupRecyclerView:
        productsAdapter = ProductsAdapter(emptyList(), RetrofitClient.BASE_URL) { product ->
            Log.e("jkl", "hj")
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
            }
            startActivity(intent)

        }
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun loadProducts() {
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing = true

        lifecycleScope.launch {
            try {
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    productsAdapter.products = products
                    productsAdapter.reload()
                } else {
                    showError("Ошибка загрузки: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("Ошибка подключения: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}