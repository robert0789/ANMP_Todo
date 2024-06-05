package com.robert.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robert.todoapp.R
import com.robert.todoapp.databinding.FragmentCreateTodoBinding
import com.robert.todoapp.databinding.FragmentEditTodoBinding
import com.robert.todoapp.databinding.FragmentTodoListBinding
import com.robert.todoapp.model.Todo
import com.robert.todoapp.viewmodel.DetailTodoViewModel

class EditTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener {

    private lateinit var binding: FragmentEditTodoBinding
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var  todo : Todo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentEditTodoBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel =ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.txtHeading.text = "Edit Todo"

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)

        binding.radiolistener = this
        binding.submitlistener = this

        observeViewModel()

//        binding.btnAdd.setOnClickListener{
//            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            todo.title = binding.txtTitle.text.toString()
//            todo.notes = binding.txtNotes.text.toString()
//            todo.priority = radio.tag.toString().toInt()
//            viewModel.update(todo)
//            Toast.makeText(context, "Todo updated", Toast.LENGTH_SHORT).show()
//
//
//        }

    }

    private fun observeViewModel(){
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            binding.todo =  it
//            binding.txtTitle.setText(it.title)
//            binding.txtNotes.setText(it.notes)
//            when(it.priority){
//                3-> binding.radioHigh.isChecked = true
//                2 ->binding.radioMedium.isChecked = true
//                1 ->binding.radioHigh.isChecked = true
//
//            }
        })
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()

    }

    override fun onTodoEditClickListener(v: View) {
        viewModel.update(binding.todo!!)
        Toast.makeText(context, "Todo updated", Toast.LENGTH_SHORT).show()
    }


}

