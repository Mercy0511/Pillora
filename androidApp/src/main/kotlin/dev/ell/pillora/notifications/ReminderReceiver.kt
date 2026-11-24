
package dev.ell.pillora.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.ell.pillora.R

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "pillora_medicine_reminders"

        const val EXTRA_MEDICINE_ID = "medicine_id"
        const val EXTRA_MEDICINE_NAME = "medicine_name"
        const val EXTRA_MEDICINE_DOSAGE = "medicine_dosage"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val medicineId =
            intent.getIntExtra(EXTRA_MEDICINE_ID, -1)

        val medicineName =
            intent.getStringExtra(EXTRA_MEDICINE_NAME)
                ?: "Your medicine"

        val dosage =
            intent.getStringExtra(EXTRA_MEDICINE_DOSAGE)
                ?: ""

        if (medicineId <= 0) {
            Log.e("PilloraReminder", "Invalid medicine ID")
            return
        }

        // Create notification channel on Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medicine Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Pillora medicine reminder notifications"
            }

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(channel)
        }

        // Check notification permission on Android 13+
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("PilloraReminder", "Notification permission not granted")
            return
        }

        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            Log.d("PilloraReminder", "Notifications are disabled")
            return
        }

        val message = if (dosage.isNotBlank()) {
            "It's time to take $medicineName. Dosage: $dosage"
        } else {
            "It's time to take $medicineName."
        }

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("💊 Medicine Reminder")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(medicineId, notification)

        } catch (e: SecurityException) {
            Log.e("PilloraReminder", "Unable to show notification", e)
        }
    }
}
