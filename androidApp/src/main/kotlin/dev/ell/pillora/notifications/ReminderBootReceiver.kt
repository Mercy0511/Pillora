
package dev.ell.pillora.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.ell.pillora.data.PilloraDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReminderBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        val pendingResult = goAsync()
        val appContext = context.applicationContext

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val database = PilloraDatabase.getDatabase(appContext)

                val activeMedicines =
                    database.medicineDao()
                        .getActiveMedicines()
                        .first()

                activeMedicines.forEach { medicine ->
                    ReminderScheduler.scheduleReminder(
                        appContext,
                        medicine
                    )
                }

                Log.d(
                    "PilloraBoot",
                    "Medicine reminders restored successfully"
                )

            } catch (e: Exception) {

                Log.e(
                    "PilloraBoot",
                    "Failed to restore medicine reminders",
                    e
                )

            } finally {
                pendingResult.finish()
            }
        }
    }
}
