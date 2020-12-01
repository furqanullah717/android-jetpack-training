package com.example.jetpack.utils

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jetpack.R

fun getProgresDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context)
        .apply {
            strokeWidth = 10f
            centerRadius = 10f
            start()
        }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(imageView: ImageView, uri: String?) {
    imageView.loadImage(uri, getProgresDrawable(imageView.context))
}