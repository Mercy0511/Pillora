package dev.ell.pillora.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.ell.pillora.R

class WaterReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (!WaterReminderScheduler.isMasterEnabled(context)) {
            return
        }

        val id = intent.getIntExtra("reminder_id", -1)

        if (id >= 0) {
            WaterReminderScheduler.scheduleNextForId(context, id)
        }

        val channelId = "water_reminder_channel"

        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Water Reminders",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        manager.createNotificationChannel(channel)

        if (
            (Build.VERSION.SDK_INT >= 33) &&
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            return
        }

        val notification = NotificationCompat.Builder(
            context,
            channelId,
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(
                intent.getStringExtra("title") ?: "Time to drink water! 💧",
            )
            .setContentText(
                intent.getStringExtra("message")
                    ?: "Stay hydrated and healthy!",
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(
            100 + id,
            notification,
        )
    }
}
