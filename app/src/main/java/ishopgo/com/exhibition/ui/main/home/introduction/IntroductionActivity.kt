package ishopgo.com.exhibition.ui.main.home.introduction

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.WebViewFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 6/23/18. HappyCoding!
 */
class IntroductionActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return IntroductionFragmentActionBar.newInstance(startupOption)
    }


    class IntroductionFragmentActionBar : BaseActionBarFragment() {

        companion object {
            fun newInstance(arg: Bundle): IntroductionFragmentActionBar {
                val f = IntroductionFragmentActionBar()
                f.arguments = arg

                return f
            }
        }

        override fun contentLayoutRes(): Int {
            return R.layout.fragment_single_content
        }

        private fun setupToolbars() {
            toolbar.setCustomTitle("Giới thiệu hội chợ")
            toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
            toolbar.setLeftButtonClickListener {
                activity?.onBackPressed()
            }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setupToolbars()
        }

        private lateinit var viewModel: IntroductionViewModel
        private val introObserver = Observer<String> {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, it)
            childFragmentManager.beginTransaction()
                    .replace(R.id.view_main_content, WebViewFragment.newInstance(extra))
                    .commit()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            viewModel = obtainViewModel(IntroductionViewModel::class.java, false)
            viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModel.introduction.observe(this, introObserver)

            viewModel.getIntroduction()
        }

    }

}