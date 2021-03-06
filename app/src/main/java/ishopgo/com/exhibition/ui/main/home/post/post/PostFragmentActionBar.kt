package ishopgo.com.exhibition.ui.main.home.post.post

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_REQUIRE
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class PostFragmentActionBar : BaseActionBarFragment() {
    private var typeManager = 0

    companion object {
        fun newInstance(params: Bundle): PostFragmentActionBar {
            val fragment = PostFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        typeManager = arguments?.getInt(EXTRA_REQUIRE, 0) ?: 0

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, PostFragment.newInstance(arguments
                        ?: Bundle()), "PostFragment").commit()
    }

    private fun setupToolbars() {
        if (typeManager == Const.AccountAction.ACTION_NEWS_MANAGER)
            toolbar.setCustomTitle("Thông tin hội chợ")
        if (typeManager == Const.AccountAction.ACTION_GENEREL_MANAGER)
            toolbar.setCustomTitle("Thông tin chung")

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(PostFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as PostFragment
                shareFragment.performSearching()
            }
        }
    }
}