package com.example.workout_logger_presentation.start_workout.components

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.workout_logger_presentation.start_workout.TimerStatus
import com.hbaez.core.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App Timer"
        private const val CHANNEL_ID_TIMER_EXPIRED = "menu_timer_expired"
        private const val CHANNEL_NAME_TIMER_EXPIRED = "Timer App Timer Expired"
        private const val TIMER_ID = 0

        @RequiresApi(Build.VERSION_CODES.S)
        fun showTimerExpired(context: Context){
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action = TimerStatus.RUNNING.toString() //AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(context,
                0, startIntent, PendingIntent.FLAG_MUTABLE)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER_EXPIRED, true)
            nBuilder.setContentTitle("Timer Expired!")
                .setContentText("Start again?")
                .addAction(R.drawable.ic_idle, "Start", startPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER_EXPIRED, CHANNEL_NAME_TIMER_EXPIRED, true)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerRunning(context: Context, wakeUpTime: Long){
            val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            stopIntent.action = TimerStatus.FINISHED.toString() //AppConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(context,
                0, stopIntent, PendingIntent.FLAG_MUTABLE)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM)

            val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, false)
            nBuilder.setContentTitle("Timer is Running.")
                .setContentText("End: ${df.format(Date(wakeUpTime))}")
                .setOngoing(true)
                .addAction(R.drawable.ic_exercise, "Stop", stopPendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, false)

            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun hideTimerNotification(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }

        private fun getBasicNotificationBuilder(context: Context, channelId: String, playSound: Boolean)
                : NotificationCompat.Builder{
            val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                .setAutoCancel(true)
            if(channelId ==  "menu_timer") nBuilder.setSmallIcon(R.drawable.ic_exercise) else nBuilder.setSmallIcon(R.drawable.ic_idle)
                .setVibrate(longArrayOf(1000L))
            if(playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(channelID: String,
                                                                  channelName: String,
                                                                  playSound: Boolean){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                                        else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.BLUE
                nChannel.enableVibration(true)
                nChannel.vibrationPattern = longArrayOf(1500L, 1500L)
                this.createNotificationChannel(nChannel)
            }
        }
    }
}