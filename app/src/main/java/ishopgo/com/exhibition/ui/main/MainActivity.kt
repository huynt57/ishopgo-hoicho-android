package ishopgo.com.exhibition.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainActivity : BaseSingleFragmentActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    private var backpressCount = 1 // backpresscount = 2 will exit application
    private val resetBackpressRunable = Runnable { backpressCount = 1 }

    private lateinit var mainViewModel: MainViewModel

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
//
//                msg?.let {
//                    resultCode = if (sharedViewModel.resolveMessage(it)) {
//                        // mark this message was processed and do not create notification
//                        Log.d(TAG, "chat message was processed: ")
//                        Activity.RESULT_CANCELED
//                    } else {
//                        // this message is not belong to this conversation, show notification
//                        Log.d(TAG, "chat message was not processed: ")
//                        Activity.RESULT_OK
//                    }
//                }
            }

        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java)
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return MainFragment()
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return
        else {
            if (backpressCount == 2)
                super.onBackPressed()
            else {
                backpressCount++
                Handler().postDelayed(resetBackpressRunable, 1500)
                Toast.makeText(this, "Ấn back một lần nữa để thoát ứng dụng !", Toast.LENGTH_SHORT).show()
            }
        }
    }

}