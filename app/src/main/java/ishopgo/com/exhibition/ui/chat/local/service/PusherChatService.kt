package ishopgo.com.exhibition.ui.chat.local.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import ishopgo.com.exhibition.ui.chat.local.service.utils.PresenceChannelListener
import ishopgo.com.exhibition.ui.chat.local.service.utils.PrivateChannelListener
import ishopgo.com.exhibition.ui.chat.local.service.utils.PublicChannelListener
import ishopgo.com.exhibition.ui.chat.local.service.utils.PusherUtils
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import ishopgo.com.exhibition.model.Const

/**
 * Created by xuanhong on 4/10/18. HappyCoding!
 */
class PusherChatService : Service() {

    companion object {
        private val TAG = "PusherChatService"

        fun getIntent(context: Context): Intent {
            return Intent(context, PusherChatService::class.java)
        }
    }

    private var mBinder = ChatBinder()

    /** Keeps track of all channel we are listening to */
    private var mChannelNames = mutableListOf<String>()
    private var mSubscribedChannel = mutableListOf<Channel>()

    private var handler: Handler? = null
    private var handlerThread = HandlerThread("message queue", Process.THREAD_PRIORITY_BACKGROUND)

    private var isPusherConnected = false
    val pusher: Pusher by lazy {
        val options = PusherOptions()
        options.setCluster(Const.Chat.PUSHER_CLUSTER)
        options.isEncrypted = false
        val authorizer = HttpAuthorizer(Const.Chat.PUSHER_AUTH_ENDPOINT)
        options.authorizer = authorizer

        Pusher(Const.Chat.PUSHER_API_KEY, options)
    }


    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: intent = [$intent]")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: intent = [$intent]")

        return super.onUnbind(intent)
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()

        handlerThread.start()
        handler = Handler(handlerThread.looper)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(connectionStateChange: ConnectionStateChange) {
                val currentState = connectionStateChange.currentState
                when (currentState) {
                    ConnectionState.CONNECTED -> {
                        Log.d(TAG, "onConnectionStateChange: connected")
                        isPusherConnected = true
                    }
                    else -> {
                        Log.d(TAG, "onConnectionStateChange: no connection")
                        isPusherConnected = false
                    }
                }

            }

            override fun onError(s: String?, s1: String?, e: java.lang.Exception?) {
                Log.d(TAG, "onError() called with: s = [$s], s1 = [$s1], e = [$e]")
            }
        }, ConnectionState.ALL)

    }

    private fun subscribePublic(channel: String) {
        if (pusher.getChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed. Subscribing it")
            handler?.let {
                if (isPusherConnected)
                    it.post { internalSubscribePublic(channel) }
                else {
                    // try again after 1 second
                    Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                    val retry = it.obtainMessage()
                    it.postDelayed({ subscribePublic(channel) }, 1000)
                }
            }
        }
    }

    private fun internalSubscribePublic(channel: String) {
        val channelListener = PublicChannelListener()
        val subscription = pusher.subscribe(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

    private fun subscribePrivate(channel: String) {
        if (pusher.getPrivateChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed")
            handler?.let {
                if (isPusherConnected)
                    it.post { internalSubscribePrivate(channel) }
                else {
                    // try again after 1 second
                    Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                    it.postDelayed({ subscribePrivate(channel) }, 1000)
                }
            }
        }
    }

    private fun internalSubscribePrivate(channel: String) {
        Log.d(TAG, "Subscribing channel: $channel")
        val channelListener = PrivateChannelListener(this@PusherChatService)
        val subscription = pusher.subscribePrivate(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

    private fun subscribePresence(channel: String) {
        if (pusher.getPresenceChannel(channel)?.isSubscribed == true) {
            Log.d(TAG, "channel $channel has subscribed")
        } else {
            Log.d(TAG, "channel $channel has not subscribed. Subscribing it")

            handler?.let {
                if (isPusherConnected)
                    it.post { internalSubscribePresence(channel) }
                else {
                    // try again after 1 second
                    Log.d(TAG, "pusher has not connected. Retry in 1s ...: ")
                    it.postDelayed({ subscribePresence(channel) }, 1000)
                }
            }
        }
    }

    private fun internalSubscribePresence(channel: String) {
        val channelListener = PresenceChannelListener()
        val subscription = pusher.subscribePresence(channel, channelListener, "new-chat")
        mSubscribedChannel.add(subscription)
        mChannelNames.add(channel)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")

        // should destroy connection here
        mSubscribedChannel.filter { it.isSubscribed }.map { channel -> channel.unbind("new-chat", { _, _, _ -> }) }
        mSubscribedChannel.clear()

        // unsubscribe from channels
        mChannelNames.map { pusher.unsubscribe(it); Log.d(TAG, "unsubcribe channel: $it") }

        // disconnect pusher
        pusher.disconnect()

        handlerThread.quit()

        super.onDestroy()
    }

    inner class ChatBinder : Binder() {

        /*
        send text message only, it can be user-typed or text-pattern
         */
        fun sendMessage(text: String) {
            Log.d(TAG, "sendMessage: text = [${text}]")
        }

        /*
        send image from inventory of shop
         */
        fun sendImagesFromInventory(url: MutableList<String>) {
            Log.d(TAG, "sendImagesFromInventory: url = [${url}]")
        }

        /*
        send image from local storage
         */
        fun sendMessage(imageUri: Uri) {
            Log.d(TAG, "sendMessage: imageUri = [${imageUri}]")
        }

        // register a channel to start listening to
        fun registerChannel(channel: String) {
            when {
                PusherUtils.isPrivateChannel(channel) -> subscribePrivate(channel)
                PusherUtils.isPublicChannel(channel) -> subscribePublic(channel)
                PusherUtils.isPresenceChannel(channel) -> subscribePresence(channel)
                else -> {
                    // ignored
                }
            }

        }

    }


}