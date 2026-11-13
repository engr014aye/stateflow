package com.saddiqueini.slateflow.data.local

import androidx.room.TypeConverter
import com.saddiqueini.slateflow.data.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority?): String {
        return priority?.name ?: Priority.MEDIUM.name
    }

    @TypeConverter
    fun toPriority(value: String?): Priority {
        return value?.let {
            try {
                Priority.valueOf(it)
            } catch (e: IllegalArgumentException) {
                Priority.MEDIUM
            }
        } ?: Priority.MEDIUM
    }
}
