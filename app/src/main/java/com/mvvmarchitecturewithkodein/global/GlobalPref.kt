package com.mvvmarchitecturewithkodein.global

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GlobalPref {
    private const val NAME = "MyApplication"
    private const val MODE = Context.MODE_PRIVATE
    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context?) {
        sharedPreferences = context?.getSharedPreferences(NAME, MODE)
    }

    //MARK: Save String data
    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: String) {
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    //MARK: Save Int data
    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: Int) {
        sharedPreferences?.edit()?.putInt(key, value)?.apply()
    }

    //MARK: Save Boolean data
    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: Boolean) {
        sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
    }

    //MARK: Save Float data
    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: Float) {
        sharedPreferences?.edit()?.putFloat(key, value)?.apply()
    }

    //MARK: Save Long data
    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: Long) {
        sharedPreferences?.edit()?.putLong(key, value)?.apply()
    }


    // Get preferences data
    fun getData(strKey: String): String {
        return sharedPreferences?.getString(strKey, "").toString()
    }

    // Get preferences data
    fun getData(strKey: String, defValue: String): String {
        return sharedPreferences?.getString(strKey, defValue).toString()
    }

    fun getDataInt(flag: String): Int? {
        return sharedPreferences?.getInt(flag, 0)
    }

    fun getDataBoolean(flag: String): Boolean? {
        return sharedPreferences?.getBoolean(flag, false)
    }

    fun getDataFloat(flag: String): Float? {
        return sharedPreferences?.getFloat(flag, 0F)
    }

    fun getDataLong(flag: String): Long? {
        return sharedPreferences?.getLong(flag, 0)
    }

    // Remove single preferences data
    @SuppressLint("CommitPrefEdits")
    fun removeData(key: String) {
        sharedPreferences?.edit()?.remove(key)?.apply()

    }

    // Remove all preferences data
    @SuppressLint("CommitPrefEdits")
    fun removeAllData() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    //MARK: Save user information in preferences
    /*fun saveUserInfo(user: LoginData?) {
        if (user != null) {
            saveData(Constants.TOKEN, Utils.chkStr(user.token))
            saveData(Constants.USER_ID, Utils.chkStr(user._id))
            saveData(Constants.ID, Utils.chkStr(user._id))
            saveData(Constants.USER_NAME, Utils.chkStr(user.userName))
            saveData(Constants.EMAIL, Utils.chkStr(user.email))
            saveData(Constants.MOBILE, Utils.chkStr(user.mobile))
            saveData(Constants.DOB, Utils.chkStr(user.dob))
            saveData(Constants.GENDER, Utils.chkStr(user.gender))
            saveData(Constants.FIRST_NAME, Utils.chkStr(user.firstName))
            saveData(Constants.LAST_NAME, Utils.chkStr(user.lastName))
            saveData(Constants.IMAGE, Utils.chkStr(user.image))
            saveData(Constants.COUNTRY_CODE, Utils.chkStr(user.countryCode))
            saveData(Constants.AMOUNT, Utils.chkInt(user.amount))
            saveData(Constants.REFERRAL_CODE, Utils.chkStr(user.referralCode))
            saveData(Constants.REFERRAL_COUNT, Utils.chkInt(user.referralCount))
            saveData(Constants.IS_REFERRAL_APPLIED, Utils.chkBoolean(user.isReferralApplied))
            saveData(Constants.REFERRAL_AMOUNT, Utils.chkInt(user.referralAmount))
        }
    }*/

    //MARK: Save array list
    fun saveArrayList(list: ArrayList<String?>?, strKey: String?) {
        sharedPreferences?.edit()?.putString(strKey, Gson().toJson(list))?.apply()
    }

    //MARK: Get array list
    fun getArrayList(strKey: String?): ArrayList<String?>? {
        return try {
            val json: String = sharedPreferences?.getString(strKey, null).toString()
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}