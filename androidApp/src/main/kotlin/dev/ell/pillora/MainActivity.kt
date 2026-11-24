package dev.ell.pillora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.ell.pillora.data.PilloraDatabase
import dev.ell.pillora.notifications.WaterReminderScheduler
import dev.ell.pillora.screens.HistoryScreen
import dev.ell.pillora.screens.HomeScreen
import dev.ell.pillora.screens.MedicinesScreen
import dev.ell.pillora.screens.ReportsScreen
import dev.ell.pillora.screens.SettingsScreen
import dev.ell.pillora.water.WaterRepository
import dev.ell.pillora.water.WaterTrackerScreen
import dev.ell.pillora.water.WaterViewModel

private val DeepGreen = Color(0xFF005640)
private val Mint = Color(0xFF91F0CC)
private val Background = Color(0xFFF5F6FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PilloraDatabase.getDatabase(applicationContext)
        WaterReminderScheduler.initializeIfNeeded(applicationContext)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = DeepGreen,
                    secondary = Mint,
                    background = Background,
                    surface = Color.White,
                ),
            ) {
                PilloraNavigation()
            }
        }
    }
}

@Composable
private fun PilloraNavigation() {
    val tabs = listOf("Home", "Medicines", "Water", "History", "Reports", "Settings")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Medication,
        Icons.Default.WaterDrop,
        Icons.Default.History,
        Icons.Default.BarChart,
        Icons.Default.Settings,
    )

    var selected by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val waterViewModel = remember(context) {
        val database = PilloraDatabase.getDatabase(context)
        val repository = WaterRepository(database.waterDao())
        WaterViewModel(repository)
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = { selected = index },
                        icon = {
                            Icon(icons[index], contentDescription = title)
                        },
                        label = { Text(title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepGreen,
                            selectedTextColor = DeepGreen,
                            indicatorColor = Mint,
                        ),
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selected) {
                0 -> HomeScreen()
                1 -> MedicinesScreen()
                2 -> WaterTrackerScreen(viewModel = waterViewModel)
                3 -> HistoryScreen()
                4 -> ReportsScreen()
                else -> SettingsScreen()
            }
        }
    }
}
