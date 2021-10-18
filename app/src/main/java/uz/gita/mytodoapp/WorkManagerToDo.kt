package uz.gita.mytodoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkManagerToDo(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        setNotification(
            inputData.getInt("id", 101),
            inputData.getString("title") as String,
            inputData.getString("description") as String
        )
        return Result.success()
    }

    private fun setNotification(id: Int, title: String, description: String) {
        val intent = Intent(this.appContext, MainActivity::class.java)
        val padIntent = TaskStackBuilder.create(appContext).run {
            addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("worker", "Work Manager", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val build = NotificationCompat.Builder(applicationContext, "worker")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_edit)
            .setContentIntent(padIntent)
        manager.notify(id, build.build())
    }
}