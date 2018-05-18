package ishopgo.com.exhibition.ui.chat.local.service.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.pusher.client.channel.PrivateChannelEventListener
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.chat.local.service.PusherMessageReceiver
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * Created by xuanhong on 12/25/17. HappyCoding!
 */
class PrivateChannelListener(var context: Context) : PrivateChannelEventListener {
    companion object {
        private val TAG = "PusherChatService"
    }

    var weakContext: WeakReference<Context>? = null

    init {
        weakContext = WeakReference(context)
    }

    override fun onEvent(channelName: String?, eventName: String?, data: String?) {
        Log.d(TAG, "onEvent $data")
        val context = weakContext?.get()
        context?.let {
            val intent = Intent(it, PusherMessageReceiver::class.java)
            intent.putExtra(Const.Chat.EXTRA_MESSAGE, data)
            intent.action = Const.Chat.PUSHER_MESSAGE
            it.startService(intent)
        }

    }

    override fun onAuthenticationFailure(messange: String?, exception: Exception?) {
        Log.d(TAG, "onAuthenticationFailure $messange")
    }

    override fun onSubscriptionSucceeded(channelName: String?) {
        Log.d(TAG, "onSubscriptionSucceeded $channelName")
    }

}