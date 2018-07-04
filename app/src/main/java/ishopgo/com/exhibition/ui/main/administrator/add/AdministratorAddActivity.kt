package ishopgo.com.exhibition.ui.main.administrator.add

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class AdministratorAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return AdministratorAddFragmentActionBar.newInstance(startupOption)
    }

    private lateinit var viewModel: FragmentAdministratorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(FragmentAdministratorViewModel::class.java)
        viewModel.showFragmentMember.observe(this, Observer {
            showCommentChild()
        })
    }

    private fun showCommentChild() {
        val params = Bundle()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_container, AdministratorMemberFragment.newInstance(params))
                .addToBackStack(AdministratorMemberFragment.TAG)
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