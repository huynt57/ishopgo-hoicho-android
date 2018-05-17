package ishopgo.com.exhibition.ui.main.questmanager

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class QuestionManagerFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): QuestionManagerFragmentActionBar {
            val fragment = QuestionManagerFragmentActionBar()
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
                .replace(R.id.view_main_content, QuestionManagerTabFragment.newInstance(Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý hỏi đáp")

        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            toast("Đang phát triển")
//            val fragment = childFragmentManager.findFragmentByTag(QuestionManagerProcessedFragment.TAG)
//            if (fragment != null) {
//                val shareFragment = fragment as QuestionManagerProcessedFragment
//                shareFragment.performSearching()
//            }
        }
    }
}