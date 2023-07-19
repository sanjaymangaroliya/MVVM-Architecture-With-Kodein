package com.mvvmarchitecturewithkodein.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mvvmarchitecturewithkodein.utils.Utils
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

// TAG - classname prints in main method only but can't prints in retrofit inner methods or others
val Any.TAG: String
    get() {
//        val tag = javaClass.simpleName
//        return if (tag.length <= 23) tag else tag.substring(0, 23)
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }


// extension function to get display metrics instance
fun Activity.displayMetrics(): DisplayMetrics {
    // display metrics is a structure describing general information
    // about a display, such as its size, density, and font scaling
    val displayMetrics = DisplayMetrics()

    if (Build.VERSION.SDK_INT >= 30) {
        display?.apply {
            getRealMetrics(displayMetrics)
        }
    } else {
        // getMetrics() method was deprecated in api level 30
        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    return displayMetrics
}

// extension function to get device width
fun Fragment.getWidth(): Int {
    var width: Int = 0
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        val display = activity?.display
        display!!.getRealMetrics(displayMetrics)
        displayMetrics.widthPixels
    } else {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        width
    }
}

// extension function to get device height
fun Fragment.getHeight(): Int {
    var height: Int = 0
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        val display = activity?.display
        display!!.getRealMetrics(displayMetrics)
        displayMetrics.heightPixels
    } else {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        height
    }
}

// extension function to get formatted number like
/*
* Input: 690 then Output: 690
* Input: 1523 then Output: 1.5k
* Input: 101808 then Output: 101.8k
* Input: 7898562 then Output: 7.9M
* */
/*fun numberFormatting(count: Long): String {
    if (count == null) return ""
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", count / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}*/

/*fun numberFormatting(count: String): String {
    if (count.isNullOrEmpty()) return ""
    if (com.roleit.global.Utils.isNotEmptyString(count).not()) return ""
    if (count.toLong() < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", count.toLong() / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}*/

fun numberFormatting(number: String): String {
    return if (Utils.isNotEmptyString(number)) {
        val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
        val numValue: Long = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0").format(
                numValue / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    } else {
        ""
    }
}

//MARK: Show keyboard when edit text is having focus
private fun EditText.showKeyboard(context: Context) {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(findFocus(), 0)
}

fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    //create span builder and replaces current text with it
    text = SpannableStringBuilder.valueOf(text).apply {
        //search for all URL spans and replace all spans with our own clickable spans
        getSpans(0, length, URLSpan::class.java).forEach {
            //add new clickable span at the same position
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClicked?.invoke(it.url)
                    }
                },
                getSpanStart(it),
                getSpanEnd(it),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            //remove old URLSpan
            removeSpan(it)
        }
    }
    //make sure movement method is set
    movementMethod = LinkMovementMethod.getInstance()
}