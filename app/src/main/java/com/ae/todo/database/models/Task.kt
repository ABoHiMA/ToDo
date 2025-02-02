package com.ae.todo.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "name")
    var title: String? = null,
    var desc: String? = null,
    var date: Long? = null,
    var time: Long? = null,
    var status: Boolean = false,
)
