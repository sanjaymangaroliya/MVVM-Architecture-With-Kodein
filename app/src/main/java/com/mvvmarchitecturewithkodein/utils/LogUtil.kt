package com.mvvmarchitecturewithkodein.utils

import android.util.Log

//MARK: When you upload your app in Play store before you make SWITCH = false
object LogUtil {
    private const val MATCH = "%s->%s->%d"
    private const val CONNECTOR = ":<--->:"
    private const val SWITCH = true
    private fun buildHeader(): String {
        val stack = Thread.currentThread().stackTrace[4]
        return if (stack == null) "UNKNOWN" else stack.lineNumber.toString() + "=>"
        //        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
//        return stack == null ? "UNKNOWN" : String.format(Locale.getDefault(),
//                MATCH, stack.getClassName(), stack.getMethodName(), stack.getLineNumber()) + CONNECTOR;
    }

    fun v(TAG: String?, msg: Any) {
        if (SWITCH) Log.v(TAG, buildHeader() + msg.toString())
    }

    fun d(TAG: String?, msg: Any) {
        if (SWITCH) Log.d(TAG, buildHeader() + msg.toString())
    }

    fun i(TAG: String?, msg: Any) {
        if (SWITCH) Log.i(TAG, buildHeader() + msg.toString())
    }

    fun w(TAG: String?, msg: Any) {
        if (SWITCH) Log.w(TAG, buildHeader() + msg.toString())
    }

    fun e(TAG: String?, msg: Any) {
        if (SWITCH) Log.e(TAG, buildHeader() + msg.toString())
    }
}