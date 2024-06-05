package com.robert.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao

//dengan interface, class bisa inherit lebih dari satu interface
//tapi gaboleh ada penjabaran terkait fungsinya
interface TodoDao {
@Insert(onConflict=OnConflictStrategy.REPLACE)
fun insertAll(vararg todo: Todo)

@Query("SELECT * FROM todo ORDER BY priority DESC")
fun selectAllTodo(): List<Todo>

@Query("SELECT * FROM todo WHERE uuid= :id")
fun selectTodo(id:Int): Todo

@Delete
fun deleteTodo(todo:Todo)

@Query("SELECT * FROM todo WHERE is_done = 0")
fun selectUnchecked() : List<Todo>
//cara 1
@Query("UPDATE todo SET title=:title, notes=:notes, priority=:priority WHERE uuid=:uuid")
fun update(title:String, notes:String, priority: Int, uuid:Int)


//cara2
@Update
fun updateTodo(todo: Todo)
}