package ishopgo.com.exhibition.service

import android.content.Intent
import android.os.Looper
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ishopgo.com.exhibition.ui.chat.local.service.PushNotificationMessageReceiver

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
                val intent = Intent(this@FcmService, PushNotificationMessageReceiver::class.java)
                data.keys.forEach { s: String? -> s?.let { intent.putExtra(it, data[it]) } }
                startService(intent)
            }
        }
    }

}