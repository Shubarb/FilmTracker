package com.example.filmtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.filmtracker.models.Movie

@Database(entities = [Movie::class], version = 3)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val MOVIE_DATABASE_NAME = "MOVIE_DATABASE_NAME"
        private var INSTANCE: MovieDatabase? = null

        val migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ${"table_call_api"} (${"id"} TEXT, ${"title"} TEXT, ${"overview"} TEXT,${"vote_average"} TEXT,${"release_date"} TEXT,${"poster_path"} TEXT,${"adult"} TEXT,${"isFavorite"} TEXT,${"reminderTime"} TEXT,${"reminderTimeDisplay"} TEXT,${"isload"} TEXT)")
            }
        }

        fun getInstance(context: Context): MovieDatabase {
            val instance = INSTANCE
            if (instance != null) {
                return instance
            }
            synchronized(this) {
                val temp = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    MOVIE_DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .addMigrations(migration)
                    .build()
                INSTANCE = temp
                return temp
            }
        }
    }
}