package com.mvvmarchitecturewithkodein.extensions

import android.app.Activity
import android.content.Context
import android.widget.Toast


fun Context.showToast(message: String) {
    if (message.isNotEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToastLong(message: String) {
    if (message.isNotEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

fun Activity.showToast(message: String) {
    if (message.isNotEmpty()) {
        this.runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.showToastLong(message: String) {
    if (message.isNotEmpty()) {
        this.runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}

/*fun customToast(text: String, activity: Activity, type: Int) {
    activity.runOnUiThread {
        val inflater: LayoutInflater = activity.layoutInflater
        val layout: View = inflater.inflate(
            R.layout.toast_custom, activity.findViewById(R.id.toast_root_layout) *//*as ViewGroup?*//*)

        val message = layout.findViewById<TextView>(R.id.toast_message)

        message?.text = text

        val constraintLayout = layout.findViewById<LinearLayout>(R.id.ll)
        val textType = layout.findViewById<TextView>(R.id.textType)
        if (type == 1) {
            constraintLayout?.backgroundTintList = activity.resources.getColorStateList(R.color.toast_green)
            textType.text = "Success"
        } else if(type==3) {
            constraintLayout?.backgroundTintList = activity.resources.getColorStateList(R.color.message_color)
            textType.text = "Message"
        }else if (type==2){
            constraintLayout?.backgroundTintList = activity.resources.getColorStateList(R.color.toast_red)
            textType.text = "Alert"
        }else if (type==4){
            constraintLayout?.backgroundTintList = activity.resources.getColorStateList(R.color.toast_green)
            textType.text = "Verification in process"
        }else{
            constraintLayout?.backgroundTintList = activity.resources.getColorStateList(R.color.toast_red)
            textType.text = "Error"
        }

        val toast = Toast(activity)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}*/

/*fun errorToast(activity: Activity) {
    activity.runOnUiThread {
        val inflater: LayoutInflater = activity.layoutInflater
        val layout: View = inflater.inflate(
            R.layout.snackbar_custom, activity.findViewById(R.id.snackbar_root_layout) *//*as ViewGroup?*//*)
        val toast = Toast(activity)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}*/

/*
fun Activity.customToastMain(text: String, type: Int) {
    val inflater: LayoutInflater = layoutInflater
    val layout: View = inflater.inflate(
        R.layout.toast_custom, findViewById(R.id.toast_root_layout) */
/*as ViewGroup?*//*
)

    val message = layout.findViewById<TextView>(R.id.toast_message)

    message?.text = text

    val constraintLayout = layout.findViewById<LinearLayout>(R.id.ll)
    val textType = layout.findViewById<TextView>(R.id.textType)
    if (type == 1) {
        constraintLayout?.backgroundTintList = resources.getColorStateList(R.color.toast_green)
        textType.text = "Success"
    } else {
        constraintLayout?.backgroundTintList = resources.getColorStateList(R.color.toast_red)
        textType.text = "Error"
    }

    val toast = Toast(applicationContext)
    toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()

}*/
