package com.robert.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.robert.todoapp.R
import com.robert.todoapp.databinding.FragmentCreateTodoBinding
import com.robert.todoapp.model.Todo
import com.robert.todoapp.util.NotificationHelper
import com.robert.todoapp.util.TodoWorker
import com.robert.todoapp.viewmodel.DetailTodoViewModel
import java.util.concurrent.TimeUnit


class CreateTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener {

    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //minta izin ke user untuk post notif
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),NotificationHelper.REQUEST_NOTIF)
            }
        }

        binding.todo = Todo("","",3,0)
        binding.radioListener = this
        binding.addListener = this

        viewModel =
            ViewModelProvider(this).get(DetailTodoViewModel::class.java)

//        binding.btnAdd.setOnClickListener {
////
//
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == NotificationHelper.REQUEST_NOTIF){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val notif = NotificationHelper(requireContext())
                notif.createNotification("Todo created", "Stay focus!")
            }
        }
    }

    override fun onTodoEditClickListener(v: View) {
//        val notif = NotificationHelper(v.context)
////            notif.createNotification("Todo created", "Stay focus!")

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(20, TimeUnit.SECONDS)
            .setInputData(
                workDataOf("title" to "Todo Created",
                    "message" to "Stay Focus")
            ).build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)

//        val radio = v.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        val todo = Todo(
//            binding.txtTitle.text.toString(),
//            binding.txtNotes.text.toString(),
//            radio.tag.toString().toInt()
//        )
        viewModel.addTodo(binding.todo!!)
        Toast.makeText(v.context, "Data added", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }


}