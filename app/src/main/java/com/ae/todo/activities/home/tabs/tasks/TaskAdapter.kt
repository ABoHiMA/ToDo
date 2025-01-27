package com.ae.todo.activities.home.tabs.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ae.todo.R
import com.ae.todo.components.Utils
import com.ae.todo.database.models.Task
import com.ae.todo.databinding.ItemTaskBinding

class TaskAdapter(val listOfTasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    var onTitleClick: OnClickListener? = null
    var onDelClick: OnClickListener? = null
    var onEditClick: OnClickListener? = null
    var onDoneClick: OnClickListener? = null

    fun interface OnClickListener {
        fun onClick(position: Int, task: Task)
    }

    fun removeTask(position: Int) {
        listOfTasks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listOfTasks.size - position)
    }

    fun changeState(position: Int) {
        listOfTasks[position].state = !listOfTasks[position].state

        notifyItemChanged(position)
    }

    inner class ViewHolder(val itemBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(task: Task) {
            val time = Utils.convertToDate(task.time!!, true)
            val date = Utils.convertToDate(task.date!!, false)

            itemBinding.contentTask.tvTitle.text = task.title
            itemBinding.contentTask.tvTime.text = time
            itemBinding.contentTask.tvDate.text = date

            if (task.state) {
                itemBinding.contentTask.imgStatus.setImageResource(R.color.onSecondaryColor)
                itemBinding.contentTask.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        itemBinding.contentTask.tvTitle.context, R.color.onSecondaryColor
                    )
                )
                itemBinding.contentTask.tvCheck.setText(R.string.done)
                itemBinding.contentTask.cardCheck.visibility = View.INVISIBLE
            } else {
                itemBinding.contentTask.imgStatus.setImageResource(R.color.primaryColor)
                itemBinding.contentTask.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        itemBinding.contentTask.tvTitle.context, R.color.primaryColor
                    )
                )
                itemBinding.contentTask.imgCheck.setImageResource(R.drawable.ic_check)
                itemBinding.contentTask.cardCheck.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = listOfTasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = listOfTasks[position]
        holder.bindData(task)

        holder.itemBinding.start.root.setOnClickListener {
            onDelClick?.onClick(position, task)
        }

        holder.itemBinding.contentTask.cardCheck.setOnClickListener {
            onDoneClick?.onClick(position, task)
        }

        holder.itemBinding.contentTask.tvCheck.setOnClickListener {
            onDoneClick?.onClick(position, task)
        }

        holder.itemBinding.end.root.setOnClickListener {
            onEditClick?.onClick(position, task)
        }

        holder.itemBinding.contentTask.tvTitle.setOnClickListener {
            onTitleClick?.onClick(position, task)
        }
    }

}