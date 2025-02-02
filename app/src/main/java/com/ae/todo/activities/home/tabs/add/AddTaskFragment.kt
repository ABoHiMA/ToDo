package com.ae.todo.activities.home.tabs.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ae.todo.R
import com.ae.todo.components.Utils.convertToDate
import com.ae.todo.components.Utils.convertToLong
import com.ae.todo.components.Utils.showDatePicker
import com.ae.todo.components.Utils.showTimePicker
import com.ae.todo.database.MyDataBase
import com.ae.todo.database.dao.TaskDao
import com.ae.todo.database.models.Task
import com.ae.todo.databinding.FragmentAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentAddTaskBinding
    private lateinit var taskDao: TaskDao
    private var calendar: Calendar = Calendar.getInstance()
    private var taskTime: Long? = null
    private var taskDate: Long? = null
    var onTaskAdded: OnTaskAdded? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskDao = MyDataBase.getInstance().taskDao()
        dateTimeButtons(view.context)
        onAddTaskClick()
    }

    private fun onAddTaskClick() {
        viewBinding.btnEnter.setOnClickListener {
            if (checkValidations()) {
                taskDao.insertTask(createTask())
                onTaskAdded?.onAddTask()
                dismiss()
                resetData()
            }
        }
    }

    private fun createTask(): Task {
        return Task(
            title = viewBinding.etTitle.text.toString(),
            desc = viewBinding.etDesc.text.toString(),
            time = taskTime,
            date = taskDate,
        )
    }

    private fun dateTimeButtons(context: Context) {
        viewBinding.btnTime.setOnClickListener {
            showTimePicker(calendar, context) { selectedTime ->
                val readableTime = convertToDate(selectedTime, true)
                viewBinding.btnTime.text = readableTime
                taskTime = selectedTime
            }
        }

        viewBinding.btnDate.setOnClickListener {
            showDatePicker(calendar, context) { selectedDate ->

                val readableDate = convertToDate(selectedDate, false)
                viewBinding.btnDate.text = readableDate
                taskDate = convertToLong(viewBinding.btnDate.text.toString(), false)
            }
        }
    }

    private fun resetData() {
        viewBinding.etTitle.text?.clear()
        viewBinding.etDesc.text?.clear()
        viewBinding.btnDate.setText(R.string.select_date)
        viewBinding.btnTime.setText(R.string.select_time)
        taskTime = null
        taskDate = null
    }

    private fun checkValidations(): Boolean {
        if (viewBinding.etTitle.length() == 0) {
            viewBinding.etTitle.error = requireActivity().getString(R.string.enter_your_task_title)
            return false
        }
        if (viewBinding.etDesc.length() == 0) {
            viewBinding.etDesc.setText(R.string.no_description)
        }
        if (viewBinding.btnTime.text == requireActivity().getString(R.string.select_time)) {
            viewBinding.tvError.text = requireActivity().getString(R.string.select_time)
            return false
        }
        if (viewBinding.btnDate.text == requireActivity().getString(R.string.select_date)) {
            viewBinding.tvError.text = requireActivity().getString(R.string.select_date)
            return false
        }
        viewBinding.tvError.text = ""
        return true
    }

    fun interface OnTaskAdded {
        fun onAddTask()
    }
}