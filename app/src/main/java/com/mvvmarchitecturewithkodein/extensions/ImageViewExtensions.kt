package com.mvvmarchitecturewithkodein.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvvmarchitecturewithkodein.R

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
        .into(this)
}

fun ImageView.loadUrl(url: String, defaultImage: Int) {
    Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

fun ImageView.loadUrlFitCenter(url: String, defaultImage: Int) {
    Glide.with(this).load(url).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

fun ImageView.loadUrlCenterCrop(url: String, defaultImage: Int) {
    Glide.with(this).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

fun ImageView.loadUrlCenterInside(url: String, defaultImage: Int) {
    Glide.with(this).load(url).centerInside().diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

fun ImageView.loadUrlCircleCrop(url: String, defaultImage: Int) {
    Glide.with(this).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

fun ImageView.loadUrlGIF(url: String, defaultImage: Int) {
    Glide.with(this).asGif().load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(defaultImage).error(defaultImage).into(this)
}

