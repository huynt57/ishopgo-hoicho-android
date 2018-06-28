package ishopgo.com.exhibition.ui.community.comment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.widget.Toast
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.community.comment.child.CommunityCommentChildFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityCommentActivity : BaseSingleFragmentActivity() {
    private lateinit var mainViewModel: CommentViewModel

    override fun createFragment(startupOption: Bundle): Fragment {
        return CommunityCommentFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = obtainViewModel(CommentViewModel::class.java)
        mainViewModel.showCommentChild.observe(this, Observer {
            it?.let { it1 -> showCommentChild(it1) }
        })
    }

    private fun showCommentChild(comment: CommunityComment) {
        val params = Bundle()
        params.putLong(Const.TransferKey.EXTRA_ID, -1L)
        params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(comment))
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_container, CommunityCommentChildFragment.newInstance(params))
                .addToBackStack(CommunityCommentChildFragment.TAG)
                .commit()
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return
        else {
            super.onBackPressed()
        }
    }
}