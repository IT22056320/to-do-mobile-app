package com.example.tpdoapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tpdoapp.database.Converters
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "tasks")
@TypeConverters(Converters::class)
@Parcelize
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val taskTitle:String,
    val taskPriority: String,
    val taskDeadline: Date?,
    val taskDesc:String


):Parcelable
