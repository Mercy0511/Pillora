package dev.ell.pillora.data

import kotlinx.coroutines.flow.Flow

class MedicineRepository(
    private val medicineDao: MedicineDao,
) {

    val allMedicines: Flow<List<Medicine>> =
        medicineDao.getAllMedicines()

    val activeMedicines: Flow<List<Medicine>> =
        medicineDao.getActiveMedicines()

    suspend fun addMedicine(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine)
    }

    suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }

    suspend fun getMedicine(id: Int): Medicine? {
        return medicineDao.getMedicineById(id)
    }

    suspend fun updateStatus(id: Int, active: Boolean) {
        medicineDao.updateMedicineStatus(id, active)
    }

    suspend fun refillStock(medicineId: Int, amount: Int) {
        medicineDao.refillStock(medicineId, amount)
    }

    suspend fun decrementStock(medicineId: Int, amount: Int = 1) {
        medicineDao.decrementStock(medicineId, amount)
    }
}
