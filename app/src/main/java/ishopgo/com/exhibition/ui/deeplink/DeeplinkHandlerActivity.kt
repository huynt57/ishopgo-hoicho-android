package ishopgo.com.exhibition.ui.deeplink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.home.post.post.detail.PostMenuDetailActivity
import ishopgo.com.exhibition.ui.main.home.post.question.detail.QuestionMenuDetailActivity
import ishopgo.com.exhibition.ui.main.map.ExpoDetailActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
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
        Log.d(TAG, "onCreate: intent = [$intent]")

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener {
                    it?.let {
                        val link = it.link
                        Log.d(TAG, "processData: qrCode = [$link]")

                        resolveLink(link)

                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "getDynamicLink error: ", it)
                }
    }

    private fun resolveLink(link: Uri): Any {
        // booth detail: http://hangviet360.com/gian-hang/gian-hang-19657?booth=19657
        val boothId = link.getQueryParameter("booth")

        // fair: http://hangviet360.com?fairId=30
        val fairId = link.getQueryParameter("fairId")

        // product: http://hangviet360.com?productId=30
        val stamp = link.getQueryParameter("stamp")

        // product: http://hangviet360.com?productId=30
        val productId = link.getQueryParameter("productId")

        // general_info + news: http://hangviet360.com?postId=30
        val postId = link.getQueryParameter("postId")

        // question: http://hangviet360.com?questionId=30
        val questionId = link.getQueryParameter("questionId")

        return when {
            boothId != null && boothId.isNotBlank() -> {
                val intent = Intent(this, ShopDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, boothId.toLong())
                startActivity(intent)
                finish()
            }
            fairId != null && fairId.isNotBlank() -> {
                Log.d("hong", "nhan dc fairId= $fairId")
                val intent = Intent(this, ExpoDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, fairId.toLong())
                startActivity(intent)
                finish()
            }
            productId != null && productId.isNotBlank() -> {
                Log.d("hong", "nhan dc productId= $productId")
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, productId.toLong())
                startActivity(intent)
                finish()
            }

            stamp != null && stamp.isNotBlank() -> {
                Log.d("hong", "nhan dc stamp= $stamp")
                val decodeBase64 = Base64.decode(stamp, Base64.DEFAULT)
                val convertUTF8 = String(decodeBase64, charset("UTF-8"))
                val link = Uri.parse("https://ishopgo.com?$convertUTF8")

                if (link.getQueryParameter("productId").isNotBlank()) {
                    val intent = Intent(this, ProductDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, link.getQueryParameter("productId").toLong())
                    intent.putExtra(Const.TransferKey.EXTRA_STAMP_CODE, link.getQueryParameter("stampCode"))
                    intent.putExtra(Const.TransferKey.EXTRA_STAMP_ID, link.getQueryParameter("stampId"))
                    if (link.getQueryParameter("type")?.isNotEmpty() == true) {
                        intent.putExtra(Const.TransferKey.EXTRA_STAMP_TYPE, link.getQueryParameter("type"))
                    }
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                finish()
            }
            postId != null && postId.isNotBlank() -> {
                Log.d("hong", "nhan dc postId= $postId")
                val i = Intent(this, PostMenuDetailActivity::class.java)
                i.putExtra(Const.TransferKey.EXTRA_ID, postId.toLong())
                startActivity(i)
                finish()
            }
            questionId != null && questionId.isNotBlank() -> {
                Log.d("hong", "nhan dc questionId= $questionId")
                val i = Intent(this, QuestionMenuDetailActivity::class.java)
                i.putExtra(Const.TransferKey.EXTRA_ID, questionId.toLong())
                startActivity(i)
                finish()
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