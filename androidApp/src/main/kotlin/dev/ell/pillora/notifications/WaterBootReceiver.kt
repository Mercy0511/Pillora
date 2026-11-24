package dev.ell.pillora.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WaterBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (
            (intent.action == Intent.ACTION_BOOT_COMPLETED) ||
            (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED)
        ) {
            if (WaterReminderScheduler.isMasterEnabled(context)) {
                WaterReminderScheduler.scheduleAll(context)
            }
        }
    }
}
