package ishopgo.com.exhibition.ui.chat.local.service.utils

import android.util.Log
import com.pusher.client.channel.ChannelEventListener

/**
 * Created by xuanhong on 12/25/17. HappyCoding!
 */
class PublicChannelListener : ChannelEventListener {
    companion object {
        private val TAG = "PusherChatService"
    }

    override fun onEvent(channelName: String?, eventName: String?, data: String?) {
        Log.d(TAG, "onEvent $data")
    }

    override fun onSubscriptionSucceeded(channelName: String?) {
        Log.d(TAG, "onSubscriptionSucceeded $channelName")
    }
}