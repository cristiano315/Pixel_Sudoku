package com.pera.sudoku.model

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    //converters for room

    //date to long
    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }

    //long to date
    @TypeConverter
    fun toDate(time: Long?): Date?{
        return time?.let { Date(it) }
    }

    //enums
    @TypeConverter
    fun fromDifficulties(value: Difficulties): String{
        return value.name
    }

    @TypeConverter
    fun toDifficulties(value: String): Difficulties{
        return Difficulties.valueOf(value)
    }

    @TypeConverter
    fun fromResults(value: Results): String{
        return value.name
    }

    @TypeConverter
    fun toResults(value: String): Results{
        return Results.valueOf(value)
    }
}