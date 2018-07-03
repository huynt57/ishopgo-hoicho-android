package ishopgo.com.exhibition.ui.deeplink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.main.MainActivity
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

                        // booth detail: http://hangviet360.com/gian-hang/gian-hang-19657?booth=19657
                        val boothId = link.getQueryParameter("booth")

                        // fair: http://hangviet360.com?fairId=30
                        val fairId = link.getQueryParameter("fairId")

                        when {
                            boothId != null && boothId.isNotBlank() -> {
                                val intent = Intent(this, ShopDetailActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_ID, boothId.toLong())
                                startActivity(intent)
                                finish()
                            }
                            fairId != null && fairId.isNotBlank() -> {
                                Log.d("hong", "nhan dc fairId= $fairId")
//                                val intent = Intent(this, ShopDetailActivity::class.java)
//                                intent.putExtra(Const.TransferKey.EXTRA_ID, boothId.toLong())
//                                startActivity(intent)
//                                finish()
                            }
                            else -> {
                                Log.d(TAG, "Không hợp lệ")
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "getDynamicLink error: ", it)
                }
    }
}