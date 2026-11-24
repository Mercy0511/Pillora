package dev.ell.pillora.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object WaterReminderScheduler {

    private const val PREFS = "water_reminder_settings"
    private const val MASTER = "master_enabled"
    private const val INITIALIZED = "scheduler_initialized"

    data class Reminder(
        val id: Int,
        val title: String,
        val hour: Int,
        val minute: Int,
        val enabled: Boolean,
    )

    private val defaults = listOf(
        Reminder(0, "Good Morning 🌅", 9, 0, true),
        Reminder(1, "Good Afternoon ☀️", 13, 0, true),
        Reminder(2, "Good Evening 🌆", 17, 0, true),
        Reminder(3, "Good Night 🌙", 20, 0, true),
    )

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun initializeIfNeeded(context: Context) {
        val p = prefs(context)
        if (!p.getBoolean(INITIALIZED, false)) {
            p.edit()
                .putBoolean(MASTER, true)
                .putBoolean(INITIALIZED, true)
                .apply()
            scheduleAll(context)
        }
    }

    fun isMasterEnabled(context: Context) =
        prefs(context).getBoolean(MASTER, true)

    fun setMasterEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(MASTER, enabled).apply()
        if (enabled) scheduleAll(context) else cancelAll(context)
    }

    fun getReminders(context: Context): List<Reminder> {
        val p = prefs(context)

        return defaults.map { item ->
            item.copy(
                hour = p.getInt("hour_${item.id}", item.hour),
                minute = p.getInt("minute_${item.id}", item.minute),
                enabled = p.getBoolean("enabled_${item.id}", item.enabled),
            )
        }
    }

    fun updateReminder(
        context: Context,
        id: Int,
        hour: Int,
        minute: Int,
        enabled: Boolean,
    ) {
        require(id in 0..3)
        require((hour in 0..23) && (minute in 0..59))

        prefs(context).edit()
            .putInt("hour_$id", hour)
            .putInt("minute_$id", minute)
            .putBoolean("enabled_$id", enabled)
            .apply()

        scheduleAll(context)
    }

    fun scheduleAll(context: Context) {
        cancelAll(context)

        if (!isMasterEnabled(context)) return

        getReminders(context)
            .filter { it.enabled }
            .forEach { scheduleNext(context, it) }
    }

    fun scheduleNext(context: Context, reminder: Reminder) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, WaterReminderReceiver::class.java).apply {
            putExtra("reminder_id", reminder.id)
            putExtra("title", reminder.title)
            putExtra("message", "Time to drink water! Stay hydrated 💧")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE,
        ) as AlarmManager

        if (
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent,
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent,
            )
        }
    }

    fun scheduleNextForId(context: Context, id: Int) {
        if (!isMasterEnabled(context)) return

        val reminder = getReminders(context)
            .find { it.id == id && it.enabled } ?: return

        scheduleNext(context, reminder)
    }

    fun cancelAll(context: Context) {
        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE,
        ) as AlarmManager

        defaults.forEach { reminder ->
            val intent = Intent(context, WaterReminderReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
