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
class PusherMessageReceiver : IntentService("PusherMessageReceiver") {

    companion object {
        private val TAG = "PusherMessageReceiver"
    }

    override fun onHandleIntent(intent: Intent?) {
        val content = intent?.getStringExtra(Const.Chat.EXTRA_MESSAGE)

        Log.d(TAG, "onHandleIntent: $content")

        content?.let {
            sendNotification(content)
        }
    }

    private fun sendNotification(json: String) {
        sendNotification(json, Bundle())
    }

    private fun sendNotification(json: String, extras: Bundle) {
        val broadcast = Intent()
        extras.putString(Const.Chat.EXTRA_MESSAGE, json)
        broadcast.putExtras(extras)
        broadcast.action = Const.Chat.BROADCAST_NOTIFICATION

        sendOrderedBroadcast(broadcast, null, FinalChatResultReceiver(), null, Activity.RESULT_OK, null, extras)
    }

}