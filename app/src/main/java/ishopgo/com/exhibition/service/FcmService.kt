package ishopgo.com.exhibition.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.extensions.asColor
import me.leolin.shortcutbadger.ShortcutBadger

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class FcmService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FcmService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.apply {
            Log.d(TAG, "onMessageReceived: looper = " + Looper.myLooper())
            // Check if message contains a data payload.
            if (data.isNotEmpty()) {
                Log.d(TAG, "Message data payload: $data")
            }

            // Check if message contains a notification payload.
            if (notification != null) {
                Log.d(TAG, "Message Notification Body: " + notification!!.body)
            }

            if (data != null && data.isNotEmpty()) {
                val notificationId = data["notification_id"]?.toInt() ?: System.currentTimeMillis().toInt()
                val title = data.getOrDefault("title", "Bạn có thông báo mới")
                val body = data.getOrDefault("content", "Nội dung thông báo")

                val i = Intent(this@FcmService, NotificationClickReceiver::class.java)
                // Put all extras
                data.keys.forEach { s: String? -> s?.let { i.putExtra(it, data[it]) } }
                val pendingIntent = PendingIntent.getBroadcast(this@FcmService, notificationId, i, PendingIntent.FLAG_UPDATE_CURRENT)

                val notification = NotificationCompat.Builder(this@FcmService, "")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setColor(R.color.colorPrimary.asColor(this@FcmService))
                        .setContentTitle(title)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                        .setLights(Color.BLUE, 500, 2000)
                        .setVibrate(longArrayOf(100, 200, 300, 400, 500))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()
                NotificationManagerCompat.from(this@FcmService).notify(data["type"], notificationId, notification)

                val badgeCount = data["badge"]?.toIntOrNull() ?: 0
                ShortcutBadger.applyCount(this@FcmService, badgeCount)

            }
        }
    }

}