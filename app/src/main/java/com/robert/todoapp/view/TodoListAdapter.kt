package com.robert.todoapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.robert.todoapp.databinding.TodoItemLayoutBinding
import com.robert.todoapp.model.Todo

class TodoListAdapter (
    val todoList:ArrayList<Todo>,
    val adapterOnClick : (Todo) -> Unit)
    : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(), TodoCheckedChangeListener, TodoEditClickListener {
    class TodoViewHolder(var binding: TodoItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var binding = TodoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return TodoViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
        //menggunakan data binding
        holder.binding.todo = todoList[position]
        holder.binding.listener = this
        holder.binding.editlistener = this
//        holder.binding.checkTask.text = todoList[position].title
//
//        holder.binding.checkTask.setOnCheckedChangeListener {
//                compoundButton, b ->
//            if(compoundButton.isPressed) {
//                adapterOnClick(todoList[position])
//            }
//        }
//
//        holder.binding.imgEdit.setOnClickListener{
//            val action = TodoListFragmentDirections.actionEditTodo(todoList[position].uuid)
//            Navigation.findNavController(it).navigate(action)
//        }

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateTodoList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onTodoCheckedChangeListener(cb: CompoundButton, isChecked: Boolean, todo: Todo) {
        if(cb.isPressed){
            if(cb.isChecked){
                todo.isDone = 1
            }
            else{
                todo.isDone = 0
            }
            adapterOnClick(todo)
        }

    }

    override fun onTodoEditClickListener(v: View) {
        val action = TodoListFragmentDirections.actionEditTodo(v.tag.toString().toInt())
        Navigation.findNavController(v).navigate(action)
    }

}
