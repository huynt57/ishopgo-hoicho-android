package ishopgo.com.exhibition.ui.chat.local.conversation

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import java.lang.Exception

/**
 * Created by xuanhong on 5/31/18. HappyCoding!
 */
class ConversationActivity : BaseSingleFragmentActivity() {

    companion object {
        private val TAG = "ConversationActivity"
    }

    private lateinit var sharedViewModel: ShareChatViewModel

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
                    resultCode = if (sharedViewModel.resolveMessage(it)) {
                        // mark this message was processed and do not create notification
                        Log.d(TAG, "chat message was processed: ")
                        Activity.RESULT_CANCELED
                    } else {
                        // this message is not belong to this conversation, show notification
                        Log.d(TAG, "chat message was not processed: ")
                        Activity.RESULT_OK
                    }
                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = obtainViewModel(ShareChatViewModel::class.java)
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return ConversationFragment.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        val bundle = intent?.extras ?: Bundle()
        val containsKey = bundle.containsKey("idConversion")
        if (containsKey) {
            val idConversation = bundle.getString("idConversion")
            bundle.putString(Const.TransferKey.EXTRA_CONVERSATION_ID, idConversation)
        }

        return bundle
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(Const.Chat.BROADCAST_NOTIFICATION)
        registerReceiver(messageReceiver, filter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(messageReceiver)
    }
}