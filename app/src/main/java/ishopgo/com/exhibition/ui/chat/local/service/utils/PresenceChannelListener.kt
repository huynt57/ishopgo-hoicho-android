package ishopgo.com.exhibition.ui.chat.local.service.utils

import android.util.Log
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.User
import java.lang.Exception

/**
 * Created by xuanhong on 12/25/17. HappyCoding!
 */
class PresenceChannelListener : PresenceChannelEventListener {
    companion object {
        private val TAG = "PusherChatService"
    }

    override fun onUsersInformationReceived(p0: String?, p1: MutableSet<User>?) {
        Log.d(TAG, "onUsersInformationReceived")
    }

    override fun userUnsubscribed(p0: String?, p1: User?) {
        Log.d(TAG, "userUnsubscribed")
    }

    override fun userSubscribed(p0: String?, p1: User?) {
        Log.d(TAG, "userSubscribed")
    }

    override fun onEvent(p0: String?, p1: String?, p2: String?) {
        Log.d(TAG, "onEvent")
    }

    override fun onAuthenticationFailure(p0: String?, p1: Exception?) {
        Log.d(TAG, "onAuthenticationFailure")
    }

    override fun onSubscriptionSucceeded(p0: String?) {
        Log.d(TAG, "onSubscriptionSucceeded")
    }

}