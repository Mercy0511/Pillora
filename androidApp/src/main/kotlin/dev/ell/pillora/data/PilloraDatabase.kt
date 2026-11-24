package dev.ell.pillora.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ell.pillora.water.WaterDao
import dev.ell.pillora.water.WaterEntry

@Database(
    entities = [Medicine::class, WaterEntry::class],
    version = 2,
    exportSchema = false,
)
abstract class PilloraDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun waterDao(): WaterDao

    companion object {

        @Volatile
        private var INSTANCE: PilloraDatabase? = null

        fun getDatabase(context: Context): PilloraDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PilloraDatabase::class.java,
                    "pillora_database",
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
