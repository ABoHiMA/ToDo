package com.ae.todo.activities.home.tabs.tasks.edit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ae.todo.R
import com.ae.todo.components.Utils.convertToDate
import com.ae.todo.components.Utils.convertToLong
import com.ae.todo.components.Utils.showDatePicker
import com.ae.todo.components.Utils.showTimePicker
import com.ae.todo.database.MyDataBase.Companion.myDataBase
import com.ae.todo.database.models.Task
import com.ae.todo.databinding.ActivityEditTaskBinding
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditTaskBinding
    private var taskId: Int? = null
    private val taskDao = myDataBase?.taskDao()
    private lateinit var task: Task
    private lateinit var taskTime: String
    private lateinit var taskDate: String
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        fillData()
        dateTimeButtons()
    }

    private fun initActivity() {
        viewBinding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        taskId = intent.getIntExtra("TaskId", -1)
        task = taskDao?.getTaskById(taskId!!)!!
        taskTime = convertToDate(task.time!!, true)
        taskDate = convertToDate(task.date!!, false)
        viewBinding.toolBar.setNavigationOnClickListener { finish() }
        viewBinding.btnSave.setOnClickListener { if (checkValidations()) updateTask() }
    }

    private fun fillData() {
        viewBinding.etTitle.setText(task.title)
        viewBinding.etDesc.setText(task.desc)
        viewBinding.btnDate.text = taskDate
        viewBinding.btnTime.text = taskTime
    }

    private fun dateTimeButtons() {
        viewBinding.btnTime.setOnClickListener {
            showTimePicker(calendar, this) { selectedTime ->
                val readableTime = convertToDate(task.time!!, true)
                viewBinding.btnTime.text = readableTime
            }
        }

        viewBinding.btnDate.setOnClickListener {
            showDatePicker(calendar, this) { selectedDate ->
                val readableDate = convertToDate(selectedDate, false)
                viewBinding.btnDate.text = readableDate
            }
        }
    }

    private fun updateTask() {
        taskDao?.updateTask(
            Task(
                id = taskId,
                title = viewBinding.etTitle.text.toString(),
                desc = viewBinding.etDesc.text.toString(),
                date = convertToLong(viewBinding.btnDate.text.toString(), false),
                time = convertToLong(viewBinding.btnTime.text.toString(), true),
                status = task.status,
            )
        )
        Toast.makeText(this, getString(R.string.task_updated), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun checkValidations(): Boolean {
        if (viewBinding.etTitle.length() == 0) {
            viewBinding.etTitle.error = this.getString(R.string.enter_your_task_title)
            return false
        }
        if (viewBinding.etDesc.length() == 0) {
            viewBinding.etDesc.error = this.getString(R.string.description)
            return false
        }
        return true
    }

}