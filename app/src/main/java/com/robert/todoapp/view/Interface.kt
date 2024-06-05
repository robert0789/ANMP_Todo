package com.robert.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.robert.todoapp.model.Todo

//bisa pake satu interface si, misal jadi interface untuk onclick
interface TodoCheckedChangeListener{
    fun onTodoCheckedChangeListener(cb:CompoundButton, isChecked: Boolean,todo:Todo)


}


interface TodoEditClickListener{
    fun onTodoEditClickListener(v: View)
}

interface RadioClickListener{
    fun onRadioClick(v:View)
}