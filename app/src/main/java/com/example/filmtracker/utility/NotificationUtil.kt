package com.example.filmtracker.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.filmtracker.models.Constant
import com.example.filmtracker.models.Movie
import com.example.filmtracker.service.AlarmReceiver

class NotificationUtil {
    fun createNotification(movie: Movie, reminderTime: Long, context: Context){
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(Constant.BUNDLE_ID_KEY,movie.id)
        intent.putExtra(Constant.BUNDLE_TITLE_KEY,movie.title)
        intent.putExtra(Constant.BUNDLE_RATE_KEY,movie.voteAverage)
        intent.putExtra(Constant.BUNDLE_RELEASE_KEY,movie.releaseDate)

        val pendingIntent = PendingIntent.getBroadcast(
            context, movie.id?:0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =  context.getSystemService(Context.ALARM_SERVICE)as AlarmManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                reminderTime,
                pendingIntent)
        }
    }

    fun cancelNotification(notificationID: Int,context: Context){
        val intent = Intent(context,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, notificationID, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =  context.getSystemService(Context.ALARM_SERVICE)as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}