package com.ae.todo.activities.home.tabs.tasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ae.todo.R
import com.ae.todo.activities.home.tabs.tasks.details.TaskDetailsActivity
import com.ae.todo.activities.home.tabs.tasks.edit.EditTaskActivity
import com.ae.todo.components.Utils.TASK_ID
import com.ae.todo.components.Utils.convertToDate
import com.ae.todo.components.Utils.convertToLong
import com.ae.todo.database.MyDataBase
import com.ae.todo.database.dao.TaskDao
import com.ae.todo.database.models.Task
import com.ae.todo.databinding.FragmentTasksBinding
import java.util.Calendar

class TaskFragment : Fragment() {
    private lateinit var viewBinding: FragmentTasksBinding
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private lateinit var taskDao: TaskDao
    private var calendar: Calendar = Calendar.getInstance()
//    private var taskTime: Long? = null
//    private var taskDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTasksBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initCalendar()
        taskDao = MyDataBase.getInstance().taskDao()
        viewBinding.btnAll.setOnClickListener {
            viewBinding.btnAll.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primaryColor
                )
            )
            viewBinding.calendarView.selectedDate = null
            getTasks()
        }
        getTasks()

    }

    private fun initCalendar() {
        viewBinding.calendarView.setOnDateChangedListener { _, date, selected ->
            calendar.set(date.year, date.month - 1, date.day)
            val dateByLong = calendar.timeInMillis
            if (selected) {
                viewBinding.btnAll.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.transparent
                    )
                )
                val dateByString = convertToDate(dateByLong, false)
                getTasksByDate(dateByString)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getTasks()
    }

    private fun initRecycler() {
        adapter = TaskAdapter(tasks)
        viewBinding.rvTasks.adapter = adapter
        adapter.onDelClick = TaskAdapter.OnClickListener { position, task ->
            adapter.removeTask(position)
            taskDao.deleteTask(task)
        }
        adapter.onDoneClick = TaskAdapter.OnClickListener { position, task ->
            adapter.changeStatus(position)
            taskDao.updateTask(task)
        }
        adapter.onEditClick = TaskAdapter.OnClickListener { position, _ ->
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra(TASK_ID, taskDao.getAllTasks()[position].id)
            startActivity(intent)
        }
        adapter.onTitleClick = TaskAdapter.OnClickListener { position, _ ->
            val intent = Intent(requireContext(), TaskDetailsActivity::class.java)
            intent.putExtra(TASK_ID, taskDao.getAllTasks()[position].id)
            startActivity(intent)
        }
    }


    private fun getTasks() {
        if (tasks.isNotEmpty()) tasks.clear()
        taskDao.getAllTasks()
        if (taskDao.getAllTasks().isNotEmpty()) {
            for (i in 0..<taskDao.getAllTasks().size) {
                tasks.add(
                    taskDao.getAllTasks()[i]
                )
            }
        }
        viewBinding.rvTasks.adapter = adapter
    }


    private fun getTasksByDate(dateByString: String) {
        if (tasks.isNotEmpty()) tasks.clear()
        val dateByLong = convertToLong(dateByString, false)
        taskDao.getTasksByDate(dateByLong)
        if (taskDao.getTasksByDate(dateByLong).isNotEmpty()) {
            for (i in 0..<taskDao.getTasksByDate(dateByLong).size) {
                tasks.add(
                    taskDao.getTasksByDate(dateByLong)[i]
                )
            }
        }
        viewBinding.rvTasks.adapter = adapter
    }

    /*

    private fun initAddFragment(context: Context) {
        bottomSheet = BottomSheetDialog(context)
        bottomSheet.setContentView(addFragmentBindingView.root)
        bottomSheet.setOnDismissListener { resetData() }
        fab.setOnClickListener { bottomSheet.show() }
    }

        private fun dateTimeButtons(context: Context) {
        addFragmentBindingView.btnTime.setOnClickListener {
            showTimePicker(calendar, context) { selectedTime ->
                val readableTime = convertToDate(selectedTime, true)
                addFragmentBindingView.btnTime.text = readableTime
                taskTime = selectedTime
            }
        }

        addFragmentBindingView.btnDate.setOnClickListener {
            showDatePicker(calendar, context) { selectedDate ->

                val readableDate = convertToDate(selectedDate, false)
                addFragmentBindingView.btnDate.text = readableDate
                taskDate = convertToLong(addFragmentBindingView.btnDate.text.toString(), false)
            }
        }
    }

        private fun addTask() {
            taskDao?.insertTask(
                Task(
                    title = addFragmentBindingView.etTitle.text.toString(),
                    desc = addFragmentBindingView.etDesc.text.toString(),
                    time = taskTime,
                    date = taskDate,
                )
            )
            getTasks()
            bottomSheet.dismiss()
            resetData()
        }
        private fun resetData() {
            addFragmentBindingView.etTitle.text?.clear()
            addFragmentBindingView.etDesc.text?.clear()
            addFragmentBindingView.btnDate.setText(R.string.select_date)
            addFragmentBindingView.btnTime.setText(R.string.select_time)
            taskTime = null
            taskDate = null
        }

        private fun checkValidations(): Boolean {
            if (addFragmentBindingView.etTitle.length() == 0) {
                addFragmentBindingView.etTitle.error =
                    requireActivity().getString(R.string.enter_your_task_title)
                return false
            }
            if (addFragmentBindingView.etDesc.length() == 0) {
                addFragmentBindingView.etDesc.setText(R.string.no_description)
            }
            if (addFragmentBindingView.btnTime.text == requireActivity().getString(R.string.select_time)) {
                addFragmentBindingView.tvError.text = requireActivity().getString(R.string.select_time)
                return false
            }
            if (addFragmentBindingView.btnDate.text == requireActivity().getString(R.string.select_date)) {
                addFragmentBindingView.tvError.text = requireActivity().getString(R.string.select_date)
                return false
            }
            addFragmentBindingView.tvError.text = ""
            return true
        }
    */

}