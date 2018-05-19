package ishopgo.com.exhibition.ui.chat.local.group.member

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/9/18. HappyCoding!
 */
class MembersActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return MembersFragment()
    }

}