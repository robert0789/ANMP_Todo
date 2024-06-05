package com.robert.todoapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robert.todoapp.util.DB_NAME
import com.robert.todoapp.util.MIGRATION_1_2
import com.robert.todoapp.util.MIGRATION_2_3

@Database(entities = arrayOf(Todo::class), version = 3)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDao(): TodoDao
    companion object{
        //function untuk konek ke DB dimana saja
        //volatile: kasitahu yang lain kalau instance sudah tidak null
        @Volatile private var instance: TodoDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                DB_NAME
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()

        //dipanggil ketika ada thread yang berusaha akses database
        operator fun invoke(context:Context) {
            if(instance!=null) {
                synchronized(LOCK) {
                    //akses ke database
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }

        }

    }
}