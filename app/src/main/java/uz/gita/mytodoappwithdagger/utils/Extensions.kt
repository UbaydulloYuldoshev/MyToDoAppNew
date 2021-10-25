package uz.gita.mytodoappwithdagger.utils


import android.annotation.SuppressLint
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
