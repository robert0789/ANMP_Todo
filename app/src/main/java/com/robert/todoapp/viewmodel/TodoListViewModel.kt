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

class TodoListViewModel (application: Application)
: AndroidViewModel(application), CoroutineScope {

    val todoListLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()


    override val coroutineContext: CoroutineContext
        //dijalankan di thread io
        get() = job + Dispatchers.IO

    fun refresh() {
        loadingLD.value = true
        todoLoadErrorLD.value = false

        //dijalankan di thread yang terpisah (multi thread)
        launch {
            val db = TodoDatabase.buildDatabase(
                getApplication()
            )

            todoListLD.postValue(db.todoDao().selectUnchecked())
            loadingLD.postValue(false)
        }
    }

    fun clearTask(todo: Todo) {
        launch {
            val db = buildDb(
                getApplication()
            )
            db.todoDao().deleteTodo(todo)

            todoListLD.postValue(db.todoDao().selectAllTodo())
        }
    }

    fun checkTask(todo: Todo){
        launch{
            val db = buildDb(
                getApplication()
            )
            db.todoDao().updateTodo(todo)
            todoListLD.postValue(db.todoDao().selectUnchecked())

        }
    }


}