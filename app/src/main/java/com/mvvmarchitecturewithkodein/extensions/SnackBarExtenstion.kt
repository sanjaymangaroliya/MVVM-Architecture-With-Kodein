package com.mvvmarchitecturewithkodein.extensions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun snackBarError(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED)
        .setTextColor(Color.WHITE).show()
}

fun snackBarSuccess(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.BLUE)
        .setTextColor(Color.WHITE).show()
}

fun showSnackBar(view: View?, message: String, type: Int) {
    if (view != null) {
        val snackBar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        when (type) {
            1 -> {
                snackBarView.setBackgroundColor(Color.parseColor("#E611AA11"))
            }
            2 -> {
                snackBarView.setBackgroundColor(Color.parseColor("#3e424b"))
            }
            3 -> {
                snackBarView.setBackgroundColor(Color.parseColor("#109D59"))
                var coordinatorLayout: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
                coordinatorLayout.gravity = Gravity.TOP
                view.alpha=0.75f
                view.layoutParams = coordinatorLayout
                snackBar.view.layoutParams=coordinatorLayout
                snackBar.animationMode= BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                snackBar.show()
            }
            4 -> {
                //Exit app
                snackBarView.setBackgroundColor(Color.parseColor("#5A5A5A"))
            }
            else -> {
                snackBarView.setBackgroundColor(Color.parseColor("#E6FA0E02"))
            }
        }
        snackBar.show()
    }
}
