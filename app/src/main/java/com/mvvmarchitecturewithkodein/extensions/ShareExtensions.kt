package com.mvvmarchitecturewithkodein.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

fun facebookSare(context: Context?, text: String?) {
    if (context != null && !text.isNullOrBlank() && !text.isNullOrEmpty()) {
        val clipboard = getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard!!.setPrimaryClip(clip)
    }
} 

