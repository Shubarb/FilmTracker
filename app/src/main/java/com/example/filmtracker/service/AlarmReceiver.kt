package com.example.filmtracker.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.filmtracker.R
import com.example.filmtracker.models.Constant

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(p0: Context, p1: Intent) {
        var movieId = 0
        var movieTitle = ""
        var movieReleaseDate = ""
        var movieRate = ""

        movieId = p1.getIntExtra(Constant.BUNDLE_ID_KEY,0)
        movieTitle = p1.getStringExtra(Constant.BUNDLE_TITLE_KEY).toString()
        movieReleaseDate = p1.getStringExtra(Constant.BUNDLE_RELEASE_KEY).toString()
        var vote = p1.getDoubleExtra(Constant.BUNDLE_RATE_KEY,0.0).toString()
        "${vote}/10".also { movieRate = it }

        Log.d("TAG","$movieId, $movieTitle, $movieReleaseDate")
        val notification = NotificationCompat.Builder(p0, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(movieTitle)
            .setContentText("Release: "+movieReleaseDate+" Rate: "+movieRate)
            .build()
        val manager = NotificationManagerCompat.from(p0);
        manager.notify(notificationID,notification)
        Log.d("AlarmReceiver","Removed")
    }
}