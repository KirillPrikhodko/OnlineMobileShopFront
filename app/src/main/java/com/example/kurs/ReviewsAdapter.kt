package com.example.kurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewsAdapter(private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.findViewById(R.id.reviewAuthor)
        val ratingText: TextView = view.findViewById(R.id.reviewRating)
            //  val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val comment: TextView = view.findViewById(R.id.reviewComment)
        val date: TextView = view.findViewById(R.id.reviewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]

        holder.author.text = review.author
        holder.ratingText.text = "${review.rating}/5"
  //      holder.ratingBar.rating = review.rating.toFloat()
        holder.comment.text = review.comment
        holder.date.text = review.date

        // Скрываем комментарий если он пустой
        holder.comment.visibility = if (review.comment.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = reviews.size

    fun updateData(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}