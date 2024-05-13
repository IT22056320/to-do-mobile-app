package com.example.tpdoapp.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        @TypeConverter
        @JvmStatic
        fun toDate(dateString: String?): Date? {
            return dateString?.let {
                dateFormat.parse(it)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): String? {
            return date?.let {
                dateFormat.format(it)
            }
        }
    }
}
