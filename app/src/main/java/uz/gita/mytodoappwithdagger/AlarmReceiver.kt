package uz.gita.mytodoappwithdagger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val builder = NotificationCompat.Builder(context!!, "todoChannelId")
            .setSmallIcon(R.drawable.ic_checklist)

            .setContentTitle(intent!!.getStringExtra("title"))
            .setContentText("You must do it, right now!")
            .setAutoCancel(true)

            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(intent.getIntExtra("id", 200), builder.build())
    }
}