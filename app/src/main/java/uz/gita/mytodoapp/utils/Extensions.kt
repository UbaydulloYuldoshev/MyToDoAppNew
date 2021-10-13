package uz.gita.mytodoapp.utils


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.app.App
import java.text.SimpleDateFormat
import java.util.*

val Long.datetime get() = SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Date(this))


@SuppressLint("SimpleDateFormat")
fun Long.toShortDate(): String {
    fun Long.toShortDate() = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(Date(this))
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis.toShortDate()
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    val yesterday = calendar.timeInMillis.toShortDate()

    val current = this.toShortDate()

    return current
}
