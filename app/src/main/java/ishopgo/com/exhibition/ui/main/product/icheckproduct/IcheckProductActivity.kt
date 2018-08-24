package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActivity

class IcheckProductActivity : BaseActivity() {
    companion object {
        private val TAG = "IcheckProductActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icheck_product_detail)
    }

    override fun requireInput(): List<String> {
        return listOf(Const.TransferKey.EXTRA_JSON)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, intent?.getStringExtra(Const.TransferKey.EXTRA_JSON))
        Navigation.findNavController(this, R.id.nav_map_host_fragment).navigate(R.id.action_icheckProductFragmentActionBar_self, extra)
    }
}