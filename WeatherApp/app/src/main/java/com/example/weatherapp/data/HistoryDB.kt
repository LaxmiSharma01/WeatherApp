package com.example.weatherapp.data

import android.content.Context
import androidx.room.*

@Database(entities = [History::class], version = 2)
@TypeConverters(Converters::class)
abstract class HistoryDB : RoomDatabase() {
    abstract fun dao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDB? = null

        fun getInstance(context: Context): HistoryDB = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDB(context)
        }

        private fun buildDB(context: Context) = Room.databaseBuilder(
            context.applicationContext, HistoryDB::class.java, "history.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherHistory(history: History)

    @Query("SELECT * FROM history ORDER BY id DESC")
    suspend fun getWeatherHistory(): List<History>

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getWeatherById(id: Long): History?
}