package com.ae.todo.activities.home.tabs.tasks.details

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ae.todo.activities.home.tabs.tasks.edit.EditTaskActivity
import com.ae.todo.components.Utils.TASK_ID
import com.ae.todo.database.MyDataBase.Companion.myDataBase
import com.ae.todo.database.models.Task
import com.ae.todo.databinding.ActivityTaskDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailsActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityTaskDetailsBinding
    private var taskId: Int? = null
    private val taskDao = myDataBase?.taskDao()
    private lateinit var task: Task
    private lateinit var taskTime: String
    private lateinit var taskDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        fillData()
    }

    override fun onRestart() {
        super.onRestart()
        fillData()
    }

    private fun initActivity() {
        viewBinding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        taskId = intent.getIntExtra("TaskId", -1)
        viewBinding.toolBar.setNavigationOnClickListener { finish() }
        viewBinding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java)
            intent.putExtra(TASK_ID, taskId)
            startActivity(intent)
        }
    }

    private fun getData() {
        task = taskDao?.getTaskById(taskId!!)!!
        taskTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(task.time!!))
        taskDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(task.date!!))
    }

    private fun fillData() {
        getData()
        viewBinding.tvTaskTitle.text = task.title
        viewBinding.tvTaskDesc.text = task.desc
        viewBinding.tvTime.text = taskTime
        viewBinding.tvDate.text = taskDate
    }

}