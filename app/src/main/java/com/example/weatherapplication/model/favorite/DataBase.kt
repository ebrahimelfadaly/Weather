package com.example.weatherapplication.model.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 3)
abstract class DataBase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    companion object {
        @Volatile
        private var instance: DataBase? = null

        fun getDatabase(context: Context): DataBase? {
            if (instance == null) {
                synchronized(DataBase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            DataBase::class.java, "Application_Database"
                        )
                            .build()
                    }
                }
            }
            return instance
        }
    }
}

