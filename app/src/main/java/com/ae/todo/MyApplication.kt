package com.ae.todo

import android.app.Application
import com.ae.todo.components.Utils.sharedPreferences
import com.ae.todo.database.MyDataBase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MyDataBase.initDB(this)
        sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)

    }
}