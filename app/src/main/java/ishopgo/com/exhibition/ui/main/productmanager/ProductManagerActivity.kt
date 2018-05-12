package ishopgo.com.exhibition.ui.main.productmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ProductManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductManagerFragmentActionBar.newInstance(startupOption)
    }
}