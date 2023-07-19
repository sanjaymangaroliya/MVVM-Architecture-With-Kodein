package com.mvvmarchitecturewithkodein.extensions

import android.view.View
import android.view.View.*

/**
 * Sets the View's visibility to [View.GONE]
 */
fun View.gone() {
    this.visibility = GONE
}

/**
 * Sets the View's visibility to [View.INVISIBLE]
 */
fun View.invisible() {
    this.visibility = INVISIBLE
}

/**
 * Sets the View's visibility to [View.VISIBLE]
 */
fun View.visible() {
    this.visibility = VISIBLE
}
