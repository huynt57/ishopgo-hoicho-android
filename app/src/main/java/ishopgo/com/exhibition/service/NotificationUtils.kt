package ishopgo.com.exhibition.service

import android.content.Context
import android.content.Intent
import android.util.Log
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.domain.response.NotificationPayload
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
object NotificationUtils {

    fun resolveNotification(context: Context, obj: Notification) {
        Log.d("NotificationUtils", "notification:  ${obj.id} ${obj.content}")
        val payload = obj.payloadData
        payload?.apply {
            when (type) {
                NotificationPayload.TYPE_PRODUCT -> showProduct(context, id)
                else -> showCommonNotification(context, obj)
            }
        }
    }

    private fun showProduct(context: Context, id: Long) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, id)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun showCommonNotification(context: Context?, obj: Notification) {
        context?.apply {

        }

    }

}