package dev.ell.pillora.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterViewModel(
    private val repository: WaterRepository,
) : ViewModel() {

    val dailyGoal = 2000

    val todayWaterIntake: StateFlow<Int> =
        repository.getTodayWaterIntake()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0,
            )

    val todayWaterEntries: StateFlow<List<WaterEntry>> =
        repository.getTodayWaterEntries()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    val allWaterEntries: StateFlow<List<WaterEntry>> =
        repository.getAllWaterEntries()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWater(amountMl)
        }
    }

    fun deleteWater(entry: WaterEntry) {
        viewModelScope.launch {
            repository.deleteWater(entry)
        }
    }
}
