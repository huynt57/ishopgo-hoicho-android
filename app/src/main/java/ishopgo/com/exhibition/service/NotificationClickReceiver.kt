package ishopgo.com.exhibition.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class NotificationClickReceiver : BroadcastReceiver() {

    val TAG = "NotificationClick"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: " + intent.extras!!)

        val extras = intent.extras
        val keys = extras!!.keySet()
        for (key in keys) {
            val value = extras.get(key)
            Log.d(TAG, "onReceive: $key: $value")
        }

        NotificationUtils.resolveNotification(context, extras ?: Bundle())
    }
}