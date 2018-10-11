package ishopgo.com.exhibition.ui.main.product.detail

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActivity

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailActivity : BaseActivity() {
    companion object {
        private val TAG = "ProductDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
    }

    override fun requireInput(): List<String> {
        return listOf(Const.TransferKey.EXTRA_ID)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val extra = Bundle()
        extra.putLong(Const.TransferKey.EXTRA_ID, intent?.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
                ?: -1L)
        Navigation.findNavController(this, R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_self, extra)
    }
}