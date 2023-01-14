package com.chefshub.utils.ext

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.chefshub.utils.R
import java.net.URL

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).placeholder(R.drawable.cheif_place_holder).into(this)
}

fun ImageView.tint(blue: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, blue),
        android.graphics.PorterDuff.Mode.MULTIPLY
    )

    this.setColorFilter(
        ContextCompat.getColor(context, blue),
        android.graphics.PorterDuff.Mode.SRC_IN
    )

}