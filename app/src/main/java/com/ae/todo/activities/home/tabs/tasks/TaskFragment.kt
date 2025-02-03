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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTasksBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDao = MyDataBase.getInstance().taskDao()
        initRecycler()
        initCalendar()
        initButtonAll()
        getTasks()
    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }

    private fun initButtonAll() {
        viewBinding.btnAll.setOnClickListener {
            viewBinding.btnAll.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.primaryColor
                )
            )
            viewBinding.calendarView.selectedDate = null
            getTasks()
        }
    }

    private fun initCalendar() {
        viewBinding.calendarView.setOnDateChangedListener { _, date, selected ->
            calendar.set(date.year, date.month - 1, date.day)
            val dateByLong = calendar.timeInMillis
            if (selected) {
                viewBinding.btnAll.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.transparent
                    )
                )
                val dateByString = convertToDate(dateByLong, false)
                getTasksByDate(dateByString)
            }
        }
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

}