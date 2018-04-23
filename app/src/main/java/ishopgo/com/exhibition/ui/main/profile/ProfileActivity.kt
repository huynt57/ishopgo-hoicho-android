package ishopgo.com.exhibition.ui.main.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class ProfileActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProfileFragment.newInstance(startupOption)
    }


}