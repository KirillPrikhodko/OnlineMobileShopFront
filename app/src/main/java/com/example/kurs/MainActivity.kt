package com.example.kurs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        setupRecyclerView()
        loadProducts()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadProducts()
        }
    }

    private fun setupRecyclerView() {
        productsAdapter = ProductsAdapter(emptyList(), RetrofitClient.BASE_URL) { product ->
            // Обработка клика по товару
//            val intent = Intent(this, ProductDetailActivity::class.java).apply {
//                putExtra("PRODUCT_ID", product.id)
//            }
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
                    productsAdapter = ProductsAdapter(products, RetrofitClient.BASE_URL) { product ->
                        // Обработка клика
                    }
                    binding.productsRecyclerView.adapter = productsAdapter
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