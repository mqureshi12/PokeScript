package com.mohammad.pokescript.utilities

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.mohammad.pokescript.R

object Image {

    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            .override(SIZE_ORIGINAL, SIZE_ORIGINAL) // Override width and height
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Cache resource for placeholder
            .into(imageView)
    }

    fun loadImageDrawable(imageView: ImageView, drawable: Int) {
        Glide.with(imageView.context)
            .load(drawable)
            .override(SIZE_ORIGINAL, SIZE_ORIGINAL) // Override width and height
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Cache resource for placeholder
            .into(imageView)
    }

    fun setMargins(view: View, left: Int, top: Int) {
        if(view.layoutParams is ViewGroup.MarginLayoutParams) {
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(left, top, 0, 0);
            view.requestLayout()
        }
    }
}