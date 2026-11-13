package com.saddiqueini.slateflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.saddiqueini.slateflow.data.model.NoteEntity
import com.saddiqueini.slateflow.data.model.TaskEntity

@Database(
    entities = [NoteEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SlateFlowDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: SlateFlowDatabase? = null

        fun getDatabase(context: Context): SlateFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SlateFlowDatabase::class.java,
                    "slateflow_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
