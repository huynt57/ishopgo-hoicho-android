package ishopgo.com.exhibition.ui.main.productmanager.add

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ProductManagerAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductManagerAddFragmentActionBar.newInstance(startupOption)
    }
}