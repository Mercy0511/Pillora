package dev.ell.pillora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SettingsMint = Color(0xFF35B89A)
private val SettingsBackground = Color(0xFFF7F8FC)

@Composable
fun SettingsScreen() {

    var remindersEnabled by remember { mutableStateOf(value = true) }
    var soundEnabled by remember { mutableStateOf(value = true) }
    var vibrationEnabled by remember { mutableStateOf(value = true) }
    var darkModeEnabled by remember { mutableStateOf(value = false) }

    var snoozeMinutes by remember { mutableStateOf("10 minutes") }
    var selectedLanguage by remember { mutableStateOf("English") }

    var showSnoozeDialog by remember { mutableStateOf(value = false) }
    var showLanguageDialog by remember { mutableStateOf(value = false) }
    var showWaterReminderSettings by remember { mutableStateOf(value = false) }

    if (showWaterReminderSettings) {
        WaterReminderSettingsScreen(
            onBack = { showWaterReminderSettings = false },
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {

        Text(
            text = "Settings",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Text(
            text = "Configure app preferences, notifications and general options",
            fontSize = 13.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSectionTitle("NOTIFICATIONS")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                SettingsSwitchRow(
                    title = "Enable Reminders",
                    subtitle = "Receive notification alarms for scheduled doses",
                    checked = remindersEnabled,
                    onCheckedChange = { remindersEnabled = it },
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsSwitchRow(
                    title = "Reminder Sound",
                    subtitle = "Play audio sound when notification fires",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it },
                    enabled = remindersEnabled,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsSwitchRow(
                    title = "Vibration",
                    subtitle = "Vibrate device for reminder alerts",
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it },
                    enabled = remindersEnabled,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsClickableRow(
                    title = "Water Reminder Alarms 💧",
                    value = "Configure",
                    onClick = { showWaterReminderSettings = true },
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsClickableRow(
                    title = "Snooze Duration",
                    value = snoozeMinutes,
                    onClick = { showSnoozeDialog = true },
                    enabled = remindersEnabled,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionTitle("PREFERENCES")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                SettingsClickableRow(
                    title = "Language",
                    value = selectedLanguage,
                    onClick = { showLanguageDialog = true },
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsSwitchRow(
                    title = "Dark Theme",
                    subtitle = "Use dark color mode across the app",
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it },
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionTitle("ABOUT")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                SettingsInfoRow("App Version", "1.0.0")

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsInfoRow("Build", "KMP Android App")

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                SettingsInfoRow("Storage", "Room Local Database")
            }
        }
    }

    if (showSnoozeDialog) {
        val options = listOf("5 minutes", "10 minutes", "15 minutes", "30 minutes")

        AlertDialog(
            onDismissRequest = { showSnoozeDialog = false },
            title = { Text("Select Snooze Duration") },
            text = {
                Column {
                    options.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            RadioButton(
                                selected = snoozeMinutes == option,
                                onClick = {
                                    snoozeMinutes = option
                                    showSnoozeDialog = false
                                },
                            )
                            Text(option)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSnoozeDialog = false }) {
                    Text("Close")
                }
            },
        )
    }

    if (showLanguageDialog) {
        val languages = listOf("English", "Spanish", "French", "German", "Hindi")

        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Select Language") },
            text = {
                Column {
                    languages.forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            RadioButton(
                                selected = selectedLanguage == lang,
                                onClick = {
                                    selectedLanguage = lang
                                    showLanguageDialog = false
                                },
                            )
                            Text(lang)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close")
                }
            },
        )
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = if (enabled) Color(0xFF263238) else Color.Gray,
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SettingsMint,
            ),
        )
    }
}

@Composable
private fun SettingsClickableRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = if (enabled) Color(0xFF263238) else Color.Gray,
        )

        TextButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            Text(
                text = value,
                color = if (enabled) SettingsMint else Color.Gray,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SettingsInfoRow(
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color(0xFF263238),
        )

        Text(
            text = value,
            fontSize = 13.sp,
            color = Color.Gray,
        )
    }
}
