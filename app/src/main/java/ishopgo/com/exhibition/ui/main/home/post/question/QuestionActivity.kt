package ishopgo.com.exhibition.ui.main.home.post.question

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class QuestionActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return QuestionFragmentActionBar.newInstance(startupOption)
    }
}