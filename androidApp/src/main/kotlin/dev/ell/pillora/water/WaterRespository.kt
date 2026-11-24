package dev.ell.pillora.water

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class WaterRepository(
    private val waterDao: WaterDao,
) {
    private fun todayDate(): String {
        return LocalDate.now().toString()
    }

    fun getTodayWaterIntake(): Flow<Int> {
        return waterDao.getTodayWaterIntake(todayDate())
    }

    fun getTodayWaterEntries(): Flow<List<WaterEntry>> {
        return waterDao.getWaterEntriesByDate(todayDate())
    }

    fun getAllWaterEntries(): Flow<List<WaterEntry>> {
        return waterDao.getAllWaterEntries()
    }

    suspend fun addWater(amountMl: Int) {
        require(amountMl > 0) {
            "Water amount must be greater than zero"
        }

        val entry = WaterEntry(
            amountMl = amountMl,
            dateKey = todayDate(),
        )

        waterDao.insertWaterEntry(entry)
    }

    suspend fun deleteWater(entry: WaterEntry) {
        waterDao.deleteWaterEntry(entry.id)
    }
}
