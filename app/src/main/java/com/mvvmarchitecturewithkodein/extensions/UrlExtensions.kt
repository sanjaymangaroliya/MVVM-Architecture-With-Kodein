package com.mvvmarchitecturewithkodein.extensions

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun createVideoFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    /* val root = File(
         Environment.getExternalStorageDirectory().toString() + File.separator + "MusicFolder", "Video")
     */
    val imageFileName: String = "RoleIt" + timeStamp + "_"
    val storageDir = File(Environment.getExternalStorageDirectory(), "/RoleIt")
    if (!storageDir.exists()) storageDir.mkdirs()
    return File.createTempFile(imageFileName, ".mp4", storageDir)
}

fun createNewAudioFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    val root = File(Environment.getExternalStorageDirectory().toString() + File.separator + "MusicFolder", "Audio")

    val imageFileName: String = "RoleIt" + timeStamp + "_"
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!
    if (!root.exists()) root.mkdirs()
    return File.createTempFile(imageFileName, ".mp3", root)
}

fun createNewFileMp3(context: Context): String {
    val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val now = Date()
    val fileName: String = formatter.format(now).toString() + ".mp3"
    try {
        val myDir = File(context.cacheDir, "RoleIt/Download")
        if (!myDir.exists()) {
            myDir.mkdir()
        }
        /*val root = File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DOWNLOADS)
        if (!root.exists()) {
            root.mkdirs()
        }*/
        val mp3File = File(myDir, fileName)
        return mp3File.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null!!
}

fun getAudioFileDir(context: Context): File {
    return File(context.cacheDir, "RoleIt/Download")
}

fun secToTime(totalSeconds: Long): String {
    return String.format("%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(totalSeconds),
        TimeUnit.MILLISECONDS.toMinutes(totalSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(
            totalSeconds)), // The change is in this line
        TimeUnit.MILLISECONDS.toSeconds(totalSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
            totalSeconds)))
}


