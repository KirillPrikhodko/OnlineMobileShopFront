package com.example.kurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductsAdapter(
    private val products: List<Product>,
    private val baseurel:String,
    private val onItemClick: (Product) -> Unit

) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val brand: TextView = view.findViewById(R.id.productBrand)
        val price: TextView = view.findViewById(R.id.productPrice)
        val stock: TextView = view.findViewById(R.id.stockStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.brand.text = "${product.brand} ${product.model}"
        holder.price.text = "%.2f ₽".format(product.price)
        holder.stock.text = if (product.stockQuantity > 0)
            "В наличии: ${product.stockQuantity} шт." else "Нет в наличии"
        holder.stock.setTextColor(
            ContextCompat.getColor(holder.itemView.context,
            if (product.stockQuantity > 0) R.color.green else R.color.red))

        // Загрузка изображения с помощью Glide
        Glide.with(holder.itemView.context)
            .load("${baseurel}${product.imageUrl}")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.image)

        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount() = products.size
}