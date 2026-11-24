package dev.ell.pillora.water

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterEntry(entry: WaterEntry): Long

    @Query("""
        SELECT COALESCE(SUM(amountMl), 0)
        FROM water_entries
        WHERE dateKey = :date
    """)
    fun getTodayWaterIntake(date: String): Flow<Int>

    @Query("""
        SELECT * FROM water_entries
        WHERE dateKey = :date
        ORDER BY timestamp DESC
    """)
    fun getWaterEntriesByDate(date: String): Flow<List<WaterEntry>>

    @Query("""
        SELECT * FROM water_entries
        ORDER BY timestamp DESC
    """)
    fun getAllWaterEntries(): Flow<List<WaterEntry>>

    @Query("DELETE FROM water_entries WHERE id = :entryId")
    suspend fun deleteWaterEntry(entryId: Int): Int
}
