package ishopgo.com.exhibition.ui.chat.local.conversation

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.util.Log
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.chat.local.service.PusherChatService
import ishopgo.com.exhibition.ui.extensions.Toolbox

/**
 * Created by xuanhong on 4/9/18. HappyCoding!
 */
class ConversationActivity : BaseSingleFragmentActivity() {
    companion object {
        private val TAG = "ConversationActivity"
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return ConversationFragment.newInstance(startupOption)
    }

    private lateinit var sharedViewModel: ConversationSharedViewModel
    private var conversationId: String = ""
    private var isServiceBound = false
    private var mChatServiceBinder: PusherChatService.ChatBinder? = null
    private val mChatServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mChatServiceBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isServiceBound = true

            mChatServiceBinder = service as PusherChatService.ChatBinder
            mChatServiceBinder?.registerChannel("private-new-channel-${UserDataManager.currentUserId}")
        }

    }

    private var messageReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: context = [${context}], intent = [${intent}]")
            intent?.let {
                val json = it.getStringExtra(Const.Chat.EXTRA_MESSAGE)
                Log.d(TAG, "onReceive: $json")

                val msg = try {
                    Toolbox.gson.fromJson(json, PusherChatMessage::class.java)
                } catch (e: Exception) {
                    null
                }

                msg?.let {
                    if (it.idConversation.equals(conversationId, true)) {
                        sharedViewModel.receiveMessage(it)

                        // mark this message was processed and do not create notification
                        Log.d(TAG, "chat message was processed: ")
                        resultCode = Activity.RESULT_CANCELED
                    } else {
                        // this message is not belong to this conversation, show notification
                        Log.d(TAG, "chat message was not processed: ")
                        resultCode = Activity.RESULT_OK
                    }
                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = obtainViewModel(ConversationSharedViewModel::class.java)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    override fun startupOptions(): Bundle {
        val bundle = Bundle()
        if (intent?.hasExtra(Const.TransferKey.EXTRA_JSON) == true) {
            val json = intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
            val localConversationItem = try {
                Toolbox.gson.fromJson(json, LocalConversationItem::class.java)
            } catch (e: Exception) {
                null
            }

            localConversationItem?.let {
                conversationId = it.idConversions ?: ""
            }
            bundle.putString(Const.TransferKey.EXTRA_JSON, json)
        } else
            finish()

        return bundle
    }

    override fun onStart() {
        super.onStart()

        bindChatService()

        val filter = IntentFilter(Const.Chat.BROADCAST_NOTIFICATION)
        registerReceiver(messageReceiver, filter)
    }

    override fun onStop() {
        if (mChatServiceBinder != null && mChatServiceBinder!!.isBinderAlive)
            unbindChatService()
        super.onStop()

        unregisterReceiver(messageReceiver)
    }

    private fun unbindChatService() {
        if (isServiceBound && mChatServiceBinder != null) {
            unbindService(mChatServiceConnection)
            isServiceBound = false
        }
    }

    private fun bindChatService() {
        if (!isServiceBound || mChatServiceBinder == null) {
            bindService(PusherChatService.getIntent(this), mChatServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

}