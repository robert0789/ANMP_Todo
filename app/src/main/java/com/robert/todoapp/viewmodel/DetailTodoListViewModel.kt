package com.robert.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robert.todoapp.model.Todo
import com.robert.todoapp.model.TodoDatabase
import com.robert.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application:  Application)
    :AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    fun addTodo(todo: Todo) {
        launch {
            val db = TodoDatabase.buildDatabase(
                getApplication()
            )
            db.todoDao().insertAll(todo)
        }
    }

    fun fetch(uuid: Int){
        launch{
             todoLD.postValue(buildDb(getApplication()).todoDao().selectTodo(uuid))
        }
    }

    fun update(todo: Todo){
        launch {
            buildDb(getApplication()).todoDao().updateTodo(todo)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}