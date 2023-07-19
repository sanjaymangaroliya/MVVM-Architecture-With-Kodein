package com.mvvmarchitecturewithkodein.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.pdf.PdfRenderer
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AnyRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mvvmarchitecturewithkodein.global.Constants
import com.mvvmarchitecturewithkodein.global.GlobalPref
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


object Utils {

    fun fullScreen(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun adjustFontScale(context: Context, configuration: Configuration) {
        if (configuration.fontScale > 1) {
            configuration.fontScale = 1f
            val metrics = context.resources.displayMetrics
            val wm = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            context.resources.updateConfiguration(configuration, metrics)
        }
    }

    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun dpToPx(context: Context, value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics
        ).toInt()
    }

    /*fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }*/

    fun logoutUser(context: Context) {
        /* GlobalPref.removeAllData()
         val intent = Intent(context, LoginActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
         context.startActivity(intent)*/
    }

    // Get current time and date
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeAndDate(inputFormat: String): String {
        if (isNotEmptyString(inputFormat)) {
            val sdf = SimpleDateFormat(inputFormat, Locale.getDefault())
            return sdf.format(Date())
        }
        return ""
    }

    fun utcTimeFormat(date: String, outPutFormat: String): String {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate: String? = try {
            val date1: Date = df.parse(date) as Date
            val outputFormatter1: DateFormat = SimpleDateFormat(outPutFormat, Locale.getDefault())
            outputFormatter1.format(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
            date
        }
        return parsedDate.toString()
    }


    fun getDayNumberSuffix(day: Int): String? {
        return if (day in 11..13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormat(date: String, inPutFormat: String, outPutFormat: String): String {
        return if (isNotEmptyString(date) && isNotEmptyString(inPutFormat) && isNotEmptyString(
                outPutFormat
            )
        ) {
            try {
                var spf = SimpleDateFormat(inPutFormat, Locale.getDefault())
                val newDate = spf.parse(date)
                spf = SimpleDateFormat(outPutFormat, Locale.getDefault())
                spf.format(newDate!!).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
                //Log.e("dateFormat", "" + e.toString())
                ""
            }
        } else {
            ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getAge(dobString: String): Int {
        if (isNotEmptyString(dobString)) {
            var date: Date? = null
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            try {
                date = sdf.parse(dobString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date == null) return 0
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date
            val year = dob[Calendar.YEAR]
            val month = dob[Calendar.MONTH]
            val day = dob[Calendar.DAY_OF_MONTH]
            dob[year, month + 1] = day
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }
            return age
        } else {
            return 0
        }
    }


    fun getLastSeen(date: String, inputFormat: String): String {
        val df: DateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("GMT")
        var time: Long = df.parse(date).time
        if (time < 1000000000000L) { // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        try {
            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = time
            val now = Calendar.getInstance()

            return when {
                now[Calendar.DATE] == smsTime[Calendar.DATE] -> {
                    " today at " + utcTimeFormat(date, Constants.HH_MM_A)
                }

                now[Calendar.DATE] - smsTime[Calendar.DATE] == 1 -> {
                    " yesterday at " + utcTimeFormat(date, Constants.HH_MM_A)
                }

                else -> {
                    " " + utcTimeFormat(date, Constants.DD_MMM_YYYY) + " at " + utcTimeFormat(
                        date, Constants.HH_MM_A
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            " " + utcTimeFormat(date, Constants.DD_MMM_YYYY) + " at " + utcTimeFormat(
                date, Constants.HH_MM_A
            )
        }

        return " " + utcTimeFormat(date, Constants.DD_MMM_YYYY) + " at " + utcTimeFormat(
            date, Constants.HH_MM_A
        )
    }

    fun convertStringToDateFormat(strData: String?): Date {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.parse(strData)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun isKeyboardOpen(context: Context?): Boolean {
        val imm =
            context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isAcceptingText
    }

    fun hideKeyboard(context: Context?) {
        if (context != null) {
            val view = (context as Activity).currentFocus
            if (view != null) {
                val inputManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun openKeyboard(context: Context?) {
        if (context != null) {
            val view = (context as Activity).currentFocus
            val methodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            assert(view != null)
            methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    fun savePushToken() {
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            } // Get new FCM registration token
            val token = task.result
            if (token.isNotEmpty()) {
                //GlobalPref.saveData(Constants.fcm_token, token)
            }
        })*/
    }

    // Check string empty or not
    fun isNotEmptyString(str: String?): Boolean {
        return !str.isNullOrEmpty() && !str.isNullOrBlank() && !str.equals(
            "null", ignoreCase = true
        ) && str != "0.0" && str != null
    }

    // Check string empty or not
    fun isEmptyString(str: String?): Boolean {
        return str.isNullOrEmpty() || str.isNullOrBlank() || str.equals(
            "null", ignoreCase = true
        ) || str == null
    }


    // Check string empty or not and return String
    fun chkStr(str: String?): String {
        if (!str.isNullOrEmpty() && !str.isNullOrBlank() && !str.equals(
                "null", ignoreCase = true
            )
        ) {
            return str
        }
        return ""
    }


    // Check string empty or not and return String
    fun chkStr(str: String?, defaultValue: String?): String {
        if (!str.isNullOrEmpty() && !str.isNullOrBlank() && !str.equals(
                "null", ignoreCase = true
            )
        ) {
            return str
        }
        return defaultValue!!
    }

    // Check parameter null or not and return
    fun chkInt(int: Int?): Int {
        if (int != null && int != 0) {
            return int
        }
        return 0
    }

    // Check parameter null or not and return
    fun chkInt(int: Int?, defaultValue: Int): Int {
        if (int != null && int != 0) {
            return int
        }
        return defaultValue
    }

    // Check parameter null or not and return
    fun chkFloat(lng: Float?): Float {
        if (lng != null) {
            return lng
        }
        return 0F
    }

    // Check parameter null or not and return
    fun chkFloat(lng: Float?, defaultValue: Float): Float {
        if (lng != null) {
            return lng
        }
        return defaultValue
    }

    // Check parameter null or not and return
    fun chkLong(lng: Long?): Long {
        if (lng != null) {
            return lng
        }
        return 0
    }

    fun chkBoolean(boolean: Boolean?): Boolean {
        if (boolean != null) {
            return boolean
        }
        return false
    }

    // Create body
    fun createBody(str: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), str)
    }

    // Create multipart
    fun createMultiPart(file: File?, key: String): MultipartBody.Part {
        if (file!!.exists()) {
            val body = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            return MultipartBody.Part.createFormData(key, "file", body)
        }
        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        return MultipartBody.Part.createFormData(key, "", body)
    }

    // Blank image pass in retrofit api
    fun blankImage(parameter: String): MultipartBody.Part {
        val requestFile = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        return MultipartBody.Part.createFormData(parameter, "", requestFile)
    }

    fun valueExists(jsonArray: JSONArray, value: String): Boolean {
        return jsonArray.toString().contains("\"$value\"")
    }

    fun getJsonFromAssets(context: Context, fileName: String?): String? {
        return try {
            val ins = context.assets.open(fileName.toString())
            val size = ins.available()
            val buffer = ByteArray(size)
            ins.read(buffer)
            ins.close()
            String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    //MARK: Delete file
    fun deleteFile(file: File?) {
        if (file?.exists() == true) {
            file.delete()
        }
    }


    //MARK: Convert Uri to File
    fun convertUriToFile(mContext: Context, uri: Uri): File? {
        var outputFile: File? = null
        val megaByte = 1024 * 1024
        try {
            val newUrl = URL(uri.toString())
            val c: HttpURLConnection = newUrl.openConnection() as HttpURLConnection
            val path = newUrl.path.split("/".toRegex()).toTypedArray()
            val fileName = path[path.size - 1]
            val myDir = File(mContext.cacheDir, "RoleIt/Images")
            if (!myDir.exists()) {
                myDir.mkdir()
            }
            outputFile = File(myDir, fileName)
            if (outputFile.exists()) {
                return outputFile
            } else {
                outputFile.createNewFile()
                c.connect()
                val `is`: InputStream = c.inputStream
                val fos = FileOutputStream(outputFile)
                val buffer = ByteArray(megaByte)
                var count: Int
                while (`is`.read(buffer).also { count = it } > 0) {
                    fos.write(buffer, 0, count)
                }
                fos.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return outputFile
    }

    //MARK: Get path from uri
    fun getPathFromUri(context: Context?, uri: Uri?): String? {
        if (context != null && uri != null) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                context.contentResolver.query(uri, projection, null, null, null) ?: return null
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val s = cursor.getString(columnIndex)
            cursor.close()
            return s
        } else {
            return ""
        }
    }

    fun getUriToDrawable(
        context: Context, @AnyRes drawableId: Int
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.resources.getResourcePackageName(
                drawableId
            ) + '/' + context.resources.getResourceTypeName(drawableId) + '/' + context.resources.getResourceEntryName(
                drawableId
            )
        )
    }

    // Check GPS status
    fun isGPSOn(context: Context?): Boolean {
        if (context != null) {
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        return false
    }

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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isOnline
    }

    fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

    /*fun getPathFromUri(context: Context?, uri: Uri?): String? {
        if (context != null && uri != null) {*/

    //MARK: To check the camera permission is granted
    fun checkCameraPermission(context: Context?): Boolean {
        if (context != null) {
            val result: Int = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun checkContactsPermission(context: Context?): Boolean {
        if (context != null) {
            val result: Int =
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    //MARK: To check the read external storage permission is granted
    fun checkReadStoragePermission(context: Context?): Boolean {
        if (context != null) {
            val result: Int = ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    //MARK: To check the write external storage permission is granted
    /*fun checkWriteStoragePermission(context: Context?): Boolean {
        if (context != null) {
            val result: Int = ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }*/

    //MARK: To check the write external storage permission is granted
    fun checkAudioStoragePermission(context: Context?): Boolean {
        if (context != null) {
            val result: Int = ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun checkLocationPermission(context: Context?): Boolean {
        if (context != null) {
            return ((ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && ((ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)))
        }
        return false
    }

    //MARK: Common dialog for permission denied
    /*fun dialogPermissionDenied(context: Context?, description: String?) {
        if (context != null && isNotEmptyString(description)) {
            val alertDialogBuilder: AlertDialog.Builder =
                AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(context.resources.getString(R.string.permission_denied))
            alertDialogBuilder.setMessage(context.resources.getString(R.string.storage_and_camera_permission_need))
            alertDialogBuilder.setPositiveButton(context.resources.getString(R.string.open_setting)) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
            }
            val dialog: AlertDialog = alertDialogBuilder.create()
            dialog.show()
        }
    }*/

    fun isVibrate(context: Context, duration: Long) {
        // val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?v!!.vibrate(duration)
    }

    /*fun shakeView(context: Context, view: View) {
        val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
        view.startAnimation(shake)
    }

    fun shakeViewGiftCoin(context: Context, view: View) {
        val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake_gift_coin)
        view.startAnimation(shake)
    }

    fun AnimateBell(context: Context, view: View) {
        val shake = AnimationUtils.loadAnimation(context, R.anim.shakeanimation)
        view.animation = shake
    }*/


    fun setSpanString(string1: String, string2: String, textView: TextView) {
        val builder = SpannableStringBuilder()
        val txtSpannable = SpannableString(string1)
        val boldSpan = StyleSpan(Typeface.BOLD)
        txtSpannable.setSpan(boldSpan, 0, string1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(txtSpannable)
        builder.append(string2)
        textView.setText(builder, TextView.BufferType.SPANNABLE)
    }

    //MARK: This method is converting the time having GMT format to simple format
    fun convertAsianTimeZone(dataDate: String?, format: String): String? {
        if (isNotEmptyString(dataDate) && isNotEmptyString(dataDate)) {
            var convTime: String? = null
            try {
                val utcFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
                utcFormat.timeZone = TimeZone.getTimeZone("UTC")
                val indianFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
                indianFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
                val timestamp = utcFormat.parse(dataDate!!)
                val output = indianFormat.format(timestamp!!)
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                val pasTime = dateFormat.parse(output)
                val nowTime = Date()
                val dateDiff = nowTime.time - pasTime!!.time
                val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
                val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
                when {
                    second < 60 -> {
                        convTime = "just now"
                    }

                    minute < 60 -> {
                        convTime = "${minute}m ago"
                    }

                    hour < 24 -> {
                        convTime = "${hour}h ago"
                    }

                    day >= 7 -> {
                        convTime = when {
                            day > 360 -> {
                                (day / 360).toString() + " years ago"
                            }

                            day > 30 -> {
                                (day / 30).toString() + " months ago"
                            }

                            else -> {
                                (day / 7).toString() + " week ago"
                            }
                        }
                    }

                    day <= 1 -> {
                        convTime = "${day}day ago"
                    }

                    day < 7 -> {
                        convTime = "${day}days ago"
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()

            }
            return convTime
        } else {
            return ""
        }
    }


    fun pdfToBitmap(pdfFile: File): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val renderer =
                PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
            val pageCount = renderer.pageCount
            if (pageCount > 0) {
                val page = renderer.openPage(0)
                bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                renderer.close()
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return bitmap
    }

    fun getRealPathFromURI(contentUri: Uri?, context: Context): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        val cursorLoader = CursorLoader(
            context, contentUri, proj, null, null, null
        )
        val cursor: Cursor = cursorLoader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    /* fun numberFormatting(value: Double): String {
         var value = value
         val suffix = " KMBT"
         var formattedNumber = ""
         val formatter: NumberFormat = DecimalFormat("#,###.#")
         val power: Int = StrictMath.log10(value).toInt()
         value /= 10.0.pow((power / 3 * 3).toDouble())
         formattedNumber = formatter.format(value)
         formattedNumber += suffix[power / 3]
         return if (formattedNumber.length > 4) formattedNumber.replace(
             "\\.[0-9]+".toRegex(),
             ""
         ) else formattedNumber
     }*/


    fun numberFormatting(number: String): String {
        return if (isNotEmptyString(number)) {
            val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
            val numValue: Long = number.toLong()
            val value = floor(log10(numValue.toDouble())).toInt()
            val base = value / 3
            if (value >= 3 && base < suffix.size) {
                DecimalFormat("#0.0").format(
                    numValue / 10.0.pow((base * 3).toDouble())
                ) + suffix[base]
            } else {
                DecimalFormat("#,##0").format(numValue)
            }
        } else {
            ""
        }
    }

    //MARK: This method is setting the margin top
    fun setMarginTop(v: View, top: Int) {
        val params = v.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(params.leftMargin, top, params.rightMargin, params.bottomMargin)
    }

    fun getLocalDate(time: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val past = format.parse(time)
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

        return when {
            seconds < 60 -> {
                ("$seconds seconds ago")
            }

            minutes < 60 -> {
                ("$minutes minutes ago")
            }

            hours < 24 -> {
                ("$hours hours ago")
            }

            else -> {
                if (days > 1) "$days days ago" else "$days day ago"
            }
        }
    }

    fun pullLinks(text: String): String {
        val links: ArrayList<String> = arrayListOf();

        val regex =
            "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        val p: Pattern = Pattern.compile(regex);
        val m: Matcher = p.matcher(text);
        while (m.find()) {
            var urlStr: String = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1);
            }
            links.add(urlStr);
        }

        val url: String = links[0]
        return url
    }

    //MARK: Encode Base64 string
    fun encodeString(input: String): String {
        return if (isNotEmptyString(input)) {
            Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
        } else {
            ""
        }
    }

    fun decodeString(input: String): String {
        return if (isNotEmptyString(input)) {
            Base64.decode("input".toByteArray(), Base64.DEFAULT).toString()
        } else {
            ""
        }
    }

    fun takeScreenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    //MARK: This method is converting the time having GMT format to simple format
    fun convertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val suffix = ""
        try {
            val utcFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val indianFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            indianFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val timestamp = utcFormat.parse(dataDate!!)
            val output = indianFormat.format(timestamp!!)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val pasTime = dateFormat.parse(output)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime!!.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            when {
                second < 60 -> {
                    convTime = "just now"
                }

                minute < 60 -> {
                    convTime = "${minute}m $suffix"
                }

                hour < 24 -> {
                    convTime = "${hour}h $suffix"
                }

                day >= 7 -> {
                    convTime = when {
                        day > 360 -> {
                            (day / 360).toString() + " years " + suffix
                        }

                        day > 30 -> {
                            (day / 30).toString() + " months " + suffix
                        }

                        else -> {
                            (day / 7).toString() + " week " + suffix
                        }
                    }
                }

                day < 7 -> {
                    convTime = "${day}days $suffix"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()

        }
        return convTime
    }

    fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }

    fun isVideoFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }

    private fun isPDFFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("application/pdf")
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun getLocalToUTCDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        return calendar.time
    }

    fun htmlText(txtView: AppCompatTextView?, str: String) {
        txtView?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(str)
        }
    }

    fun priceFormat(price: Int): String {
        val formatter: NumberFormat = DecimalFormat("#,##,###")
        return formatter.format(price)
    }

    fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {
        this@typeWrite.text = ""
        lifecycleOwner.lifecycleScope.launch {
            repeat(text.length) {
                delay(intervalMs)
                this@typeWrite.text = text.take(it + 1)
            }
        }
    }

    fun copyToClipboard(context: Context?, text: CharSequence) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

    //MARK: Check is user login or not
    fun isUserLogin(): Boolean {
        val token = GlobalPref.getData(Constants.TOKEN)
        val userId = GlobalPref.getData(Constants.TOKEN)
        if (isNotEmptyString(token) && isNotEmptyString(userId)) {
            return true
        }
        return false
    }
}