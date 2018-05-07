package ishopgo.com.exhibition.ui.community.comment

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityCommentActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return CommunityCommentFragmentActionBar.newInstance(intent.getLongExtra("post_id", 0))
    }
}