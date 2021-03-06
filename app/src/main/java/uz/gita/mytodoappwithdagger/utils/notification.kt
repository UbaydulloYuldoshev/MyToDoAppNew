package uz.gita.mytodoappwithdagger.utils

import android.app.NotificationManager
import android.content.Context
import uz.gita.mytodoappwithdagger.app.App

private val notificationManager by lazy {
    App.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
//private val CHANNEL_ID = "MyApp"
//val pendingIntent: PendingIntent = PendingIntent.getActivity(App.instance, 0, intent, 0)
//val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//    .setSmallIcon(R.drawable.ic_launcher_foreground)
//    .setContentTitle("My notification $count")
//    .setContentText("My first notification")
//    .setContentIntent(pendingIntent)
//    .setAutoCancel(true)
//    .setPriority(NotificationCompat.DEFAULT_SOUND)
//notificationManager.notify(count, notification.build())
//count++
//
//}
//fun createNotificationChannel(title : String,descriptionText:String) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val name = title
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//            description = descriptionText
//        }
//        notificationManager.createNotificationChannel(channel)
//    }
//}