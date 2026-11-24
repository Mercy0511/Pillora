package dev.ell.pillora.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.ell.pillora.notifications.WaterReminderScheduler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun WaterReminderSettingsScreen(
    onBack: (() -> Unit)? = null,
) {

    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    var masterEnabled by remember {
        mutableStateOf(
            WaterReminderScheduler.isMasterEnabled(context),
        )
    }

    var reminders by remember {
        mutableStateOf(
            WaterReminderScheduler.getReminders(context),
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = "Water Reminder Settings 💧",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text("Daily Water Reminders")
                        Text(
                            "Enable or disable all reminders",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Switch(
                        checked = masterEnabled,
                        onCheckedChange = { enabled ->
                            masterEnabled = enabled
                            WaterReminderScheduler.setMasterEnabled(
                                context,
                                enabled,
                            )
                        },
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            reminders.forEach { reminder ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {

                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                reminder.title,
                                style = MaterialTheme.typography.titleMedium,
                            )

                            val time = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, reminder.hour)
                                set(Calendar.MINUTE, reminder.minute)
                            }

                            val formattedTime = timeFormat.format(time.time)

                            Text(
                                formattedTime,
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            TextButton(
                                enabled = masterEnabled,
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hour, minute ->
                                            WaterReminderScheduler.updateReminder(
                                                context,
                                                reminder.id,
                                                hour,
                                                minute,
                                                reminder.enabled,
                                            )

                                            reminders =
                                                WaterReminderScheduler.getReminders(context)
                                        },
                                        reminder.hour,
                                        reminder.minute,
                                        false,
                                    ).show()
                                },
                            ) {
                                Text("Change Time")
                            }
                        }

                        Switch(
                            checked = reminder.enabled,
                            enabled = masterEnabled,
                            onCheckedChange = { enabled ->

                                WaterReminderScheduler.updateReminder(
                                    context,
                                    reminder.id,
                                    reminder.hour,
                                    reminder.minute,
                                    enabled,
                                )

                                reminders =
                                    WaterReminderScheduler.getReminders(context)
                            },
                        )
                    }
                }
            }
        }
    }
}
