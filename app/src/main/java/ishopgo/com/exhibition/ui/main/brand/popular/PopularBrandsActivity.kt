package ishopgo.com.exhibition.ui.main.brand.popular

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularBrandsActivity: BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return PopularBrandsFragmentActionBar.newInstance(startupOption)
    }


}