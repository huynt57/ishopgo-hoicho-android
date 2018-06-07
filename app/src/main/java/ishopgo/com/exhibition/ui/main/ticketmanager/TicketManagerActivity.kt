package ishopgo.com.exhibition.ui.main.ticketmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class TicketManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return TicketManagerFragmentActionBar.newInstance(startupOption)
    }
}