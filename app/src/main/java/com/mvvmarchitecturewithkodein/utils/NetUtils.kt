package com.mvvmarchitecturewithkodein.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.lang.Exception

object NetUtils {

    //Check for connectivity
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        var isOnline = false
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            isOnline = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                //LogUtils.displayLog("TAG","Is network Available: "+capabilities);
                capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isOnline
    }




}