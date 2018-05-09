package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ProductManagerDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductManagerDetailFragmentActionBar.newInstance(intent.getLongExtra("product_Id", 0), startupOption)
    }
}