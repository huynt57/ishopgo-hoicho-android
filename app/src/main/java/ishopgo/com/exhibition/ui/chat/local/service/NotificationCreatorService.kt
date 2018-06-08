package ishopgo.com.exhibition.ui.chat.local.service

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.NotificationPayload
import ishopgo.com.exhibition.service.NotificationClickReceiver
import ishopgo.com.exhibition.ui.extensions.asColor


/**
 * Created by xuanhong on 12/25/17. HappyCoding!
 *
 * create notification for chat message
 */
class NotificationCreatorService : IntentService("NotificationCreatorService") {
    companion object {
        private val TAG = "NotiCreatorService"
    }

    override fun onHandleIntent(intent: Intent?) {
        // create notification herer
        intent?.let {
            Log.d(TAG, "onHandleIntent ${it.extras ?: Bundle()}")
            sendNotification(it.extras ?: Bundle())
        }

    }

    private fun sendNotification(extras: Bundle) {
        Log.d(TAG, "sendNotification: extras = [${extras}]")

        val notificationId = extras.getString("notification_id")?.toInt()
                ?: System.currentTimeMillis().toInt()
        val title = extras.getString("title") ?: "Bạn có thông báo mới"
        val body = extras.getString("content") ?: extras.getString("body") ?: ""
        val type = extras.getString("type") ?: NotificationPayload.TYPE_COMMON

        val i = Intent(this@NotificationCreatorService, NotificationClickReceiver::class.java)
        i.putExtras(extras)
        val pendingIntent = PendingIntent.getBroadcast(this@NotificationCreatorService, notificationId, i, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this@NotificationCreatorService, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(R.color.colorPrimary.asColor(this@NotificationCreatorService))
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setLights(Color.BLUE, 500, 2000)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        NotificationManagerCompat.from(this@NotificationCreatorService).notify(type, notificationId, notification)
//        val badgeCount = data["badge"]?.toIntOrNull() ?: 0
//        ShortcutBadger.applyCount(this@FcmService, badgeCount)

    }
}