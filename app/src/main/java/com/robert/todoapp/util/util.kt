package com.robert.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.CaseMap.Title
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.robert.todoapp.R
import com.robert.todoapp.model.TodoDatabase
import com.robert.todoapp.view.MainActivity

val DB_NAME="newtododb"

fun buildDb(context: Context): TodoDatabase{
    val db = TodoDatabase.buildDatabase(context)
    return db
}

val MIGRATION_1_2 = object :Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority " +
                "INTEGER DEFAULT 3 NOT NULL")
    }

}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        //is_done column is using integer because mySQL does not have Boolean datatype, so we compensate it using integer

        database.execSQL("ALTER TABLE todo ADD COLUMN is_done " +
                "INTEGER DEFAULT 0 NOT NULL")    }
}

class NotificationHelper(val context: Context){
    private val CHANNEL_ID="todo_channer"
    private val NOTIFICATION_ID = 1

    companion object{
        //unique identifier for request noti
        val REQUEST_NOTIF = 100

    }

    private fun createNotificationChannel(){
        val channel  = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Channel to publish a notification when create new todo"
        val manager = context.getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification(title: String, message: String){
        createNotificationChannel()
        //pas noti muncul, user pencet, intent yang ngarahin ke main  activity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        //noti manager akan mengatur intent bila app mati
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.checklist)
            .setLargeIcon(icon).setContentTitle(title)
            .setContentText(message)
            .setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(icon).bigLargeIcon(null)
        )
            .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
        try {
            NotificationManagerCompat
                .from(context).notify(NOTIFICATION_ID, notif)
        }
        catch (e:SecurityException){
            Log.e("errornotif", e.message.toString())
        }

    }


}

class TodoWorker(context: Context, params:WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        val title = inputData.getString("title").toString()
        val message = inputData.getString("message").toString()
        NotificationHelper(applicationContext).createNotification(title, message)
        return Result.success()

    }

}
