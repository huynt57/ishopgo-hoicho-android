package ishopgo.com.exhibition.ui.main.myqr

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return MyQrFragmentActionBar.newInstance(startupOption)
    }

}