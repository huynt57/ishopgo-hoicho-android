package ishopgo.com.exhibition.ui.chat.local.service

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.widget.Toolbox


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
            val json = intent.getStringExtra(Const.Chat.EXTRA_MESSAGE)
            Log.d(TAG, "onHandleIntent $intent: $json")
            json?.let { sendNotification(json, Bundle()) }
        }

    }

    private fun sendNotification(json: String, extras: Bundle) {
        val gson = Toolbox.getDefaultGson()
        val pusherChatMessage = gson.fromJson(json, PusherChatMessage::class.java)

        val builder = NotificationCompat.Builder(this, "")
        builder.setContentTitle(pusherChatMessage.name ?: "Bạn có tin nhắn mới")
        builder.setContentText(pusherChatMessage.apiContent ?: "")
        builder.setSmallIcon(R.mipmap.ic_launcher)

        val notifyIntent = Intent(this, ConversationActivity::class.java)

        val noti = LocalConversationItem()
        noti.idConversions = pusherChatMessage.idConversation
        noti.name = pusherChatMessage.name
        notifyIntent.putExtra(Const.TransferKey.EXTRA_JSON, gson.toJson(noti))

        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        notificationCompat.flags = notificationCompat.flags or Notification.FLAG_AUTO_CANCEL
        notificationCompat.defaults = Notification.DEFAULT_ALL
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(
                pusherChatMessage.idConversation,
                pusherChatMessage.from.toInt(),
                notificationCompat
        )

    }
}