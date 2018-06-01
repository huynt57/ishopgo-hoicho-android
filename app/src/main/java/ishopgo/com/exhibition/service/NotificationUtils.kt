package ishopgo.com.exhibition.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import ishopgo.com.exhibition.domain.response.NotificationPayload
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
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
                val productId = extras.getLong("id")
                showProduct(context, productId)
            }
            NotificationPayload.TYPE_CHAT -> {
                val conversationId = extras.getString("idConversion")
                conversationId?.let {
                    showConversation(context, it)
                }
            }
            else -> {
                showCommonNotification(context, extras)
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

    private fun showCommonNotification(context: Context?, obj: Bundle) {
        context?.apply {

        }

    }

}