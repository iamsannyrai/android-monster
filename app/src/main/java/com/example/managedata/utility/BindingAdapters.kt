package com.example.managedata.utility

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.NumberFormat

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("price")
fun getItemPrice(view: TextView, value: Double) {
    val formatter = NumberFormat.getCurrencyInstance()
    val price = "${formatter.format(value)}/ each"
    view.text = price
}
