package com.example.tpdoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tpdoapp.model.Task

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Database migration logic goes here
        database.execSQL("ALTER TABLE tasks ADD COLUMN taskPriority TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE tasks ADD COLUMN taskDeadline TEXT NOT NULL DEFAULT ''")
    }
}

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase:RoomDatabase() {
    abstract fun getTaskDao():TaskDao

    companion object {
        @Volatile
        private var instance: TaskDatabase? = null
        private val LOCK = Any()



        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_db"
            ).addMigrations(MIGRATION_1_2)
             .build()
    }
}