package ishopgo.com.exhibition.ui.chat.local.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class MemberProfileActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return MemberProfileFragment()
    }

}