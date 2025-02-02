package com.ae.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ae.todo.database.models.Task

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("Select * from Task")
    fun getAllTasks(): List<Task>

    @Query("Select * from Task where date = :date")
    fun getTasksByDate(date: Long): List<Task>

    @Query("Select * from Task where status = 0")
    fun getDoneTasks(): List<Task>

    @Query("Select * from Task where id = :id")
    fun getTaskById(id: Int): Task

}