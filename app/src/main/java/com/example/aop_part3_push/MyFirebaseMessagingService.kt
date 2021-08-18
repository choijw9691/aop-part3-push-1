package com.example.aop_part3_push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    override fun onMessageReceived(remotemessage: RemoteMessage) {
        super.onMessageReceived(remotemessage)
        createNotificationChannel()

        val type = remotemessage.data["type"]
            ?.let { NotificationType.valueOf(it) }

        val title = remotemessage.data["title"]
        val message = remotemessage.data["message"]

        type ?: return



        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }

    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("notificationType","${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //onNewIntent에서 오버라이딩 해줘서 상세구현(MainActivity)
        }
        val pendingIntent = PendingIntent.getActivity(this,type.id,intent,FLAG_UPDATE_CURRENT) //여기서 id 값을 주면서 다른 푸시들을 구분해줌 ex) 일반,커스텀 등등 구분

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {

            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트빅텍스트")
                )
            }
            NotificationType.CUSTOM -> {

                notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        )
                            .apply {
                                setTextViewText(R.id.title, title)
                                setTextViewText(R.id.message, message)
                            }
                    )
            }

        }
        return notificationBuilder.build()
    }


    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }

}