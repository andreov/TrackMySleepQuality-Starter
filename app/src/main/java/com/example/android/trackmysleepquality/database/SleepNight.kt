package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* В этой задаче вы определяете night of sleep как аннотированный класс данных, представляющий сущность базы данных.
Для одной ночи сна вам нужно записать время начала, время окончания и рейтинг качества.
И вам нужен идентификатор, чтобы однозначно идентифицировать эту ночь. */

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
        @PrimaryKey(autoGenerate = true)
        var nightId: Long = 0L,

        @ColumnInfo(name = "start_time_milli")
        val startTimeMilli: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli: Long = startTimeMilli,

        @ColumnInfo(name = "quality_rating")
        var sleepQuality: Int = -1
)
