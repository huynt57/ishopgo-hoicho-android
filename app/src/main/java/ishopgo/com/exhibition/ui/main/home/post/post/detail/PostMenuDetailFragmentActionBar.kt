package ishopgo.com.exhibition.ui.main.home.post.post.detail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class PostMenuDetailFragmentActionBar : BaseActionBarFragment() {
    companion object {
        fun newInstance(params: Bundle): PostMenuDetailFragmentActionBar {
            val fragment = PostMenuDetailFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, PostMenuDetailFragment.newInstance(arguments
                        ?: Bundle()), "PostMenuDetailFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thông tin chi tiết")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_share_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(PostMenuDetailFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as PostMenuDetailFragment
                shareFragment.sharePost()
            }
        }
    }
}
