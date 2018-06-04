package ishopgo.com.exhibition.ui.deeplink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity

/**
 * Created by xuanhong on 6/4/18. HappyCoding!
 */
class DeeplinkHandlerActivity : BaseActivity() {

    companion object {
        private val TAG = "DeeplinkHandlerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener {
                    it?.let {
                        val link = it.link
                        Log.d(TAG, "processData: qrCode = [$link]")
                        val boothId = link.getQueryParameter("booth")
                        if (boothId != null && boothId.isNotBlank()) {
                            val intent = Intent(this, ShopDetailActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, boothId.toLong())
                            startActivity(intent)
                        } else
                            Log.d(TAG, "Không hợp lệ")

                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "getDynamicLink error: ", it)
                }
    }
}