package ishopgo.com.exhibition.ui.main.ticket

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class TicketActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return TicketFragmentActionBar.newInstance(startupOption)
    }
}