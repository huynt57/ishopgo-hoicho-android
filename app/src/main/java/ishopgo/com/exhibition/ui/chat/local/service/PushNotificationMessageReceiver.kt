package ishopgo.com.exhibition.ui.chat.local.service

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import ishopgo.com.exhibition.model.Const


/**
 * Created by xuanhong on 12/29/17. HappyCoding!
 * service to handle incoming message from pusher
 */
class PushNotificationMessageReceiver : IntentService("PushNotificationMessageReceiver") {

    companion object {
        private val TAG = "PushNotiMessageReceiver"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: ${intent?.extras ?: Bundle()}")
        sendNotification(intent?.extras ?: Bundle())
    }

    private fun sendNotification(extras: Bundle) {
        val broadcast = Intent()
        broadcast.putExtras(extras)
        broadcast.action = Const.Chat.BROADCAST_NOTIFICATION

        sendOrderedBroadcast(broadcast, null, FinalChatResultReceiver(), null, Activity.RESULT_OK, null, extras)
    }

}