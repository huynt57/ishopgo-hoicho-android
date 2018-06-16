package ishopgo.com.exhibition.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.domain.response.NotificationPayload
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.main.notification.detail.NotificationDetailActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
object NotificationUtils {

    fun resolveNotification(context: Context, extras: Bundle) {
        Log.d("NotificationUtils", "notification:  ${extras}")
        val type = extras.getString("type")
        when (type) {
            NotificationPayload.TYPE_PRODUCT -> {
                val productId = extras.get("id") as? Long
                productId?.let { showProduct(context, it) }
            }
            NotificationPayload.TYPE_CHAT -> {
                val conversationId = extras.getString("idConversion")
                conversationId?.let { showConversation(context, it) }
            }
            else -> {
                val notificationId = extras.get("id") as? Long
                notificationId?.let { showCommonNotification(context, it) }
            }
        }
    }

    fun resolveNotification(context: Context, notification: Notification) {
        Log.d("NotificationUtils", "notification:  $notification")
        val type = notification.payloadData?.type ?: -1
        when (type) {
            NotificationPayload.TYPE_PRODUCT -> {
                showProduct(context, notification.id)
            }
            NotificationPayload.TYPE_CHAT -> {
                // we do not have notification for chat now
//                val conversationId = extras.getString("idConversion")
//                conversationId?.let {
//                    showConversation(context, it)
//                }
            }
            else -> {
                showCommonNotification(context, notification.id)
            }
        }
    }

    private fun showConversation(context: Context, id: String) {
        val intent = Intent(context, ConversationActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, id)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    private fun showProduct(context: Context, id: Long) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, id)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun showCommonNotification(context: Context, notificationId: Long) {
        val intent = Intent(context, NotificationDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, notificationId)
        context.startActivity(intent)

    }

}