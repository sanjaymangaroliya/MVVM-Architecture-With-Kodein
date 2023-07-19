package com.mvvmarchitecturewithkodein.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

fun <T> Activity.openActivity(it: Class<T>) {
    val intent = Intent(this, it)
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}
fun <T> Activity.openActivity(it: Class<T>, bundle: Bundle) {
    val intent = Intent(this, it)
    intent.putExtras(bundle)
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Activity.openActivityWithClearTask(it: Class<T>) {
    val intent = Intent(this, it)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Activity.openActivityWithClearTask(it: Class<T>, bundle: Bundle) {
    val intent = Intent(this, it)
    intent.putExtras(bundle)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Activity.openNewActivity(it: Class<T>) {
    val intent = Intent(this, it)
    startActivity(intent)
    finish()
    finishAffinity()
}

fun <T> Activity.openActivity(it: Class<T>, bundle: Bundle, requestCode: Int) {
    val intent = Intent(this, it)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Fragment.openActivity(context: Context,it: Class<T>) {
    val intent = Intent(context, it)
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Fragment.openActivity(context: Context,it: Class<T>, bundle: Bundle) {
    val intent = Intent(context, it)
    intent.putExtras(bundle)
    startActivity(intent)
    //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}

fun <T> Fragment.openActivity(activity: Activity, it: Class<T>, bundle: Bundle, requestCode: Int) {
    val intent = Intent(activity, it)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
    //activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
}