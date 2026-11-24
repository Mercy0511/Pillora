package dev.ell.pillora.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine): Long

    @Update
    suspend fun updateMedicine(medicine: Medicine): Int

    @Delete
    suspend fun deleteMedicine(medicine: Medicine): Int

    @Query("SELECT * FROM medicines ORDER BY createdAt DESC")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE id = :medicineId")
    suspend fun getMedicineById(medicineId: Int): Medicine?

    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveMedicines(): Flow<List<Medicine>>

    @Query("UPDATE medicines SET isActive = :active WHERE id = :medicineId")
    suspend fun updateMedicineStatus(
        medicineId: Int,
        active: Boolean,
    ): Int

    @Query("UPDATE medicines SET stock = stock + :amount WHERE id = :medicineId")
    suspend fun refillStock(medicineId: Int, amount: Int): Int

    @Query("UPDATE medicines SET stock = CASE WHEN stock >= :amount THEN stock - :amount ELSE 0 END WHERE id = :medicineId")
    suspend fun decrementStock(medicineId: Int, amount: Int = 1): Int
}
