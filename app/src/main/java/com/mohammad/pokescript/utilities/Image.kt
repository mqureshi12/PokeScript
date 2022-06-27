package com.mohammad.pokescript.utilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.mohammad.pokescript.R
import android.view.ViewGroup.MarginLayoutParams

object Image {

    fun loadImage(context: Context, ImageView: ImageView, url: String) {
        Glide.with(context)
            .load(url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(ImageView);
    }

    fun loadImageDrawable(context: Context, ImageView: ImageView, drawable: Int) {
        Glide.with(context)
            .load(drawable)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(ImageView);
    }

    // Set custom position using margins for map view
    fun setMargins(view: View, left: Int, top: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, 0, 0)
            view.requestLayout()
        }
    }
}