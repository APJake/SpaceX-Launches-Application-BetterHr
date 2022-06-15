package com.apjake.spacexlaunchesbetterhr.common.util

import android.view.View
import android.widget.ImageView
import com.apjake.spacexlaunchesbetterhr.R
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


fun ImageView.show(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_background)
        .into(this)
}

fun View.showSnackBar(message: String){
    Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .show()
}