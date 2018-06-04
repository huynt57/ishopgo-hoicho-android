package ishopgo.com.exhibition.ui.main.home.post.question

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class QuestionFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): QuestionFragmentActionBar {
            val fragment = QuestionFragmentActionBar()
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
                .replace(R.id.view_main_content, QuestionFragment.newInstance(Bundle()), "QuestionFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách hỏi đáp")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }


        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(QuestionFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as QuestionFragment
                shareFragment.performSearching()
            }
        }

//        if (UserDataManager.currentUserId > 0) {
//            toolbar.rightButton2(R.drawable.ic_add_green_24dp)
//            toolbar.setRight2ButtonClickListener {
//                val fragment = childFragmentManager.findFragmentByTag(QuestionFragment.TAG)
//                if (fragment != null) {
//                    toast("Đang phát triển")
//                    val shareFragment = fragment as QuestionFragment
//                    shareFragment.performSearching()
//                }
//            }
//        }
    }
}