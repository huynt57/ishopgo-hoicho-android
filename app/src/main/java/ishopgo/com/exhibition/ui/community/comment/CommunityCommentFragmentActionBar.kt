package ishopgo.com.exhibition.ui.community.comment

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityCommentFragmentActionBar : BaseActionBarFragment() {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommunityCommentFragment.newInstance(arguments ?: Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thảo luận trong bài viết")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }

    companion object {
        fun newInstance(params: Bundle): CommunityCommentFragmentActionBar {
            val fragment = CommunityCommentFragmentActionBar()
            fragment.arguments = params
            return fragment
        }
    }
}