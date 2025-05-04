package ru.mipt.annstase.hw2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mipt.annstase.hw2.data.local.dao.TagDao
import ru.mipt.annstase.hw2.data.local.dao.TaskDao
import ru.mipt.annstase.hw2.data.local.entity.TagEntity
import ru.mipt.annstase.hw2.data.local.entity.TaskEntity
import ru.mipt.annstase.hw2.data.local.entity.TaskTagCrossRef

@Database(
    entities = [TaskEntity::class, TagEntity::class, TaskTagCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tasks_db"
            ).build()
    }
}
