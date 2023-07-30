package com.example.filmtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.filmtracker.models.Movie

@Database(entities = [Movie::class], version = 2)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun getNoteDao(): FavoriteDao
    companion object{
        @Volatile
        private var instance: FavoriteDatabase ?=null

        fun getInstance(context: Context): FavoriteDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context,FavoriteDatabase::class.java,"FavoriteDataBase").build()
            }
            return instance!!
        }
    }
}