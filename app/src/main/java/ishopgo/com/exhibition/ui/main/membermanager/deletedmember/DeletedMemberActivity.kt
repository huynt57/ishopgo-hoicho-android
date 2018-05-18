package ishopgo.com.exhibition.ui.main.membermanager.deletedmember

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class DeletedMemberActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return DeletedMemberFragmentActionBar.newInstance(startupOption)
    }
}