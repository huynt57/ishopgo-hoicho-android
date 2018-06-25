package ishopgo.com.exhibition.ui.survey

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.account.AccountViewModel
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class SurveyFragmentActionBar : BaseActionBarFragment() {
    private lateinit var viewModel: AccountViewModel

    companion object {

        fun newInstance(params: Bundle): SurveyFragmentActionBar {
            val fragment = SurveyFragmentActionBar()
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
                .replace(R.id.view_main_content, SurveyFragment.newInstance(arguments ?: Bundle()))
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(AccountViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.loggedOut.observe(this, Observer { m ->
            m?.let {
                UserDataManager.deleteUserInfo()
                toast("Đăng xuất thành công")
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)
                activity?.finish()
            }
        })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Làm bài khảo sát")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        if (arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, null) == Const.TransferKey.EXTRA_REQUIRE) {
            toolbar.setLeftButtonClickListener {
                activity?.finish()
            }
        } else {
            toolbar.setLeftButtonClickListener {
                viewModel.logout()
            }
        }
    }
}