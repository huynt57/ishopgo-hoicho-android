package ishopgo.com.exhibition.ui.main.membermanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class MemberManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return MemberManagerFragmentActionBar.newInstance(startupOption)
    }
}