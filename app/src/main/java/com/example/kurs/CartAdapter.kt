package com.example.kurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val onCartUpdated: () -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productName)
        private val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val removeButton: View = itemView.findViewById(R.id.removeButton)
        private val decreaseButton: View = itemView.findViewById(R.id.decreaseButton)
        private val increaseButton: View = itemView.findViewById(R.id.increaseButton)

        fun bind(item: CartItem) {
            nameTextView.text = item.name
            priceTextView.text = "%.2f ₽".format(item.price * item.quantity)
            quantityTextView.text = item.quantity.toString()

            Glide.with(itemView)
                .load(RetrofitClient.BASE_URL+ item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(productImage)

            removeButton.setOnClickListener {
                CartManager.removeFromCart(item.productId)
                onCartUpdated()
                notifyDataSetChanged()

            }

            decreaseButton.setOnClickListener {
                CartManager.decreaseQuantity(item.productId)
                onCartUpdated()
                if (CartManager.getItemCount(item.productId) > 0) {
                    quantityTextView.text = CartManager.getItemCount(item.productId).toString()
                    priceTextView.text = "%.2f ₽".format(item.price * CartManager.getItemCount(item.productId))
                } else {
                    notifyDataSetChanged()
                }

            }

            increaseButton.setOnClickListener {
                CartManager.increaseQuantity(item.productId)
                onCartUpdated()
                quantityTextView.text = CartManager.getItemCount(item.productId).toString()
                priceTextView.text = "%.2f ₽".format(item.price * CartManager.getItemCount(item.productId))

            }
        }
    }
}

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}