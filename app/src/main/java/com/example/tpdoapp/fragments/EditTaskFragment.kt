package com.example.tpdoapp.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tpdoapp.MainActivity
import com.example.tpdoapp.R
import com.example.tpdoapp.databinding.FragmentEditTaskBinding
import com.example.tpdoapp.model.Task
import com.example.tpdoapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EditTaskFragment : Fragment(R.layout.fragment_edit_task),MenuProvider {

    private var editTaskBinding:FragmentEditTaskBinding?=null
    private val binding get() = editTaskBinding

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var currentTask:Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding=FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost =requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Attach click listener to the deadline EditText
        binding?.editNoteDeadline?.setOnClickListener {
            showDatePickerDialog()
        }
        tasksViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding?.let {
            it.editNoteTitle.setText(currentTask.taskTitle)
            it.editNoteDesc.setText(currentTask.taskDesc)
            it.editNotePriority.setText(currentTask.taskPriority)
            // Format the date string
            val formattedDate = dateFormat.format(currentTask.taskDeadline)
            it.editNoteDeadline.setText(formattedDate)

            it.editNoteFab.setOnClickListener{ s ->
                val taskTitle = it.editNoteTitle.text.toString().trim()
                val taskDesc = it.editNoteDesc.text.toString().trim()
                val taskPriority=it.editNotePriority.text.toString().trim()
                val taskDeadlineString = it.editNoteDeadline.text.toString().trim()
                val taskDeadline:Date?=dateFormat.parse(taskDeadlineString)

                if(taskTitle.isNotEmpty()){
                    val task=Task(currentTask.id,taskTitle,taskPriority,taskDeadline,taskDesc)
                    tasksViewModel.updateTask(task)
                    view.findNavController().popBackStack(R.id.homeFragment,false)


                }else{
                    Toast.makeText(context,"Please enter task title",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Format the selected date string
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                binding?.editNoteDeadline?.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
    private fun deleteTask(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task?")
            setPositiveButton("Delete"){_,_->
                tasksViewModel.deleteTask(currentTask)
                Toast.makeText(context,"Task Deleted",Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment,false)


            }
            setNegativeButton("Cancel",null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_task,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
                true
            } else -> false


        }
    }
        override fun onDestroy() {
            super.onDestroy()
            editTaskBinding=null
    }


}