package com.example.kurs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kurs.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val apiService = RetrofitClient.apiService
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId == -1) {
            Toast.makeText(this, "Ошибка загрузки товара", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        loadProductDetails()
        loadReviews()

        // Настройка Toolbar с кнопкой "Назад"
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    // Обработка нажатия кнопки "Назад" в Toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        reviewsAdapter = ReviewsAdapter(emptyList())
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductDetailActivity)
            adapter = reviewsAdapter
        }
    }

    private fun loadProductDetails() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = apiService.getProductById(productId)
                if (response.isSuccessful) {
                    val product = response.body()
                    product?.let { updateUI(it) }
                } else {
                    showError("Ошибка загрузки товара: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("Ошибка подключения: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadReviews() {
        lifecycleScope.launch {
            try {
                val response = apiService.getProductReviews(productId)
                if (response.isSuccessful) {
                    val reviews = response.body() ?: emptyList()
                    reviewsAdapter.updateData(reviews)
                }
            } catch (e: Exception) {
                showError("Ошибка загрузки отзывов")
            }
        }
    }

    private fun updateUI(product: Product) {
        binding.apply {
            productName.text = product.name
            productBrandModel.text = "${product.brand} ${product.model}"
            productPrice.text = "%.2f ₽".format(product.price)
            productDescription.text = product.description
            stockQuantity.text = if (product.stockQuantity > 0)
                "В наличии: ${product.stockQuantity} шт." else "Нет в наличии"

            Glide.with(this@ProductDetailActivity)
                .load("${RetrofitClient.BASE_URL}${product.imageUrl}")
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(productImage)

            addToCartButton.setOnClickListener {
                // Добавляем товар в корзину
                CartManager.addToCart(product)

                // Показываем уведомление
                Toast.makeText(this@ProductDetailActivity, "${product.name} добавлен в корзину", Toast.LENGTH_SHORT).show()

                // Открываем экран корзины
                openCartActivity()

            }
        }
    }

    private fun openCartActivity() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)

        // Если нужно закрыть текущий экран:
        // finish()
    }
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}