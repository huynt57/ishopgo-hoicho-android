package ishopgo.com.exhibition.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.model.UserDataManager

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

        val type = extras.getString("type")
        val id = extras.getString("id").toLong()
        val phone = extras.getString("phone")

        val latestPhone = UserDataManager.currentUserPhone
        NotificationUtils.resolveNotification(context, Notification())
    }
}