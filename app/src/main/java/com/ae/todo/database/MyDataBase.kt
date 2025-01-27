package com.ae.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ae.todo.database.dao.TaskDao
import com.ae.todo.database.models.Task

@Database(entities = [Task::class], version = 1)
abstract class MyDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        var myDataBase: MyDataBase? = null
        private const val DATABASE_NAME = "Tasks"

        fun initDB(applicationContext: Context) {
            if (myDataBase == null) {
                myDataBase =
                    Room.databaseBuilder(applicationContext, MyDataBase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
        }

        fun getInstance(): MyDataBase {
            return myDataBase!!
        }

    }
}