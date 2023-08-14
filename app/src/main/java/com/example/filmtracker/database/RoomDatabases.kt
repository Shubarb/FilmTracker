package com.example.filmtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.filmtracker.models.Movie

@Database(entities = [Movie::class], version = 1)
abstract class RoomDatabases: RoomDatabase() {
    abstract fun getNoteDao(): FavoriteDao
    abstract fun getRemindDao(): RemindDao
    companion object{
        @Volatile
        private var instance: RoomDatabases ?=null

        fun getInstance(context: Context): RoomDatabases{
            if (instance == null){
                instance = Room.databaseBuilder(context,RoomDatabases::class.java,"RoomDataBases").build()
            }
            return instance!!
        }
    }

}