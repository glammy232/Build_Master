package com.tower.buildmaster.ror.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.tower.buildmaster.BuildMasterActivity
import com.tower.buildmaster.ror.presentation.app.BuildMasterApp
import com.google.firebase.messaging.RemoteMessage
import com.tower.buildmaster.R
import com.google.firebase.messaging.FirebaseMessagingService

private const val BM_CHANNEL_ID = "bm_notifications"
private const val BM_CHANNEL_NAME = "BM Notifications"
private const val BM_NOT_TAG = "BM"

class BuildMasterPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                chickenShowNotification(it.title ?: BM_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                chickenShowNotification(it.title ?: BM_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            chickenHandleDataPayload(remoteMessage.data)
        }
    }

    private fun chickenShowNotification(title: String, message: String, data: String?) {
        val chickenNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                BM_CHANNEL_ID,
                BM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            chickenNotificationManager.createNotificationChannel(channel)
        }

        val chickenIntent = Intent(this, BuildMasterActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            Intent.getIntent(data).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chickenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            chickenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val chickenNotification = NotificationCompat.Builder(this, BM_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.chicken_noti)
            .setAutoCancel(true)
            .setContentIntent(chickenPendingIntent)
            .build()

        chickenNotificationManager.notify(System.currentTimeMillis().toInt(), chickenNotification)
    }

    private fun chickenHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}