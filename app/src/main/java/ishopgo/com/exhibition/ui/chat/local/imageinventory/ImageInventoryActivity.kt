package ishopgo.com.exhibition.ui.chat.local.imageinventory

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 12/31/17. HappyCoding!
 */
class ImageInventoryActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ImageInventoryFragment()
    }

}