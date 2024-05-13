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

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a new table with the desired schema changes
        database.execSQL("CREATE TABLE IF NOT EXISTS tasks_new (id INTEGER PRIMARY KEY NOT NULL, taskTitle TEXT NOT NULL, taskDesc TEXT NOT NULL, taskPriority TEXT NOT NULL DEFAULT '', taskDeadline INTEGER NOT NULL DEFAULT 0)")

        // Copy data from the existing table to the new table
        database.execSQL("INSERT INTO tasks_new (id, taskTitle, taskDesc, taskPriority, taskDeadline) SELECT id, taskTitle, taskDesc, taskPriority, CAST(taskDeadline AS INTEGER) FROM tasks")

        // Drop the existing table
        database.execSQL("DROP TABLE IF EXISTS tasks")

        // Rename the new table to the original table name
        database.execSQL("ALTER TABLE tasks_new RENAME TO tasks")
    }
}




@Database(entities = [Task::class], version = 3)
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
            ).addMigrations(MIGRATION_1_2,MIGRATION_2_3)
             .build()
    }
}