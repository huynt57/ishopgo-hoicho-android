package ishopgo.com.exhibition.ui.main.questmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class QuestionManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return QuestionManagerFragmentActionBar.newInstance(startupOption)
    }
}