package ishopgo.com.exhibition.ui.chat.local.service

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by xuanhong on 4/11/18. HappyCoding!
 * the last broadcast receiver receive incoming message from pusher
 */
class FinalChatResultReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = "FinalChatResultReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val resultCode = resultCode

        if (resultCode == Activity.RESULT_OK) {
            // broadcast message was not handled by application (app is not active)
            // create notification here
            Log.d(TAG, "app is not active or this message is not for current conversation: generate notification")

            val push = Intent(context, NotificationCreatorService::class.java)
            push.putExtras(getResultExtras(true))
            context?.startService(push)

        } else {
            // broadcast message was handled by app (app is active)
            // do nothing here

            Log.d(TAG, "app is active and in current conversation: handled incoming message")
        }
    }


}