package ishopgo.com.exhibition.ui.main.account

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.account.password.ChangePasswordActivity
import ishopgo.com.exhibition.ui.main.profile.ProfileActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {

    private val adapter = AccountMenuAdapter()
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view.context)
                .load(UserDataManager.currentUserAvatar)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.error_placeholder))
                .into(view_avatar)
        view_name.text = UserDataManager.currentUserName
        view_introduce.text = "Khách tham quan - ${UserDataManager.currentUserPhone}"

        view_recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing, false))

        view_profile_current.setOnClickListener {
            openProfile()
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<AccountMenuProvider> {
            override fun click(position: Int, data: AccountMenuProvider, code: Int) {
                handleClick(data)
            }

        }
    }

    private fun handleClick(data: AccountMenuProvider) {
        val action = data.provideAction()
        when (action) {
            Const.AccountAction.ACTION_PROFILE -> {
                openProfile()
            }
            Const.AccountAction.ACTION_CHANGE_PASSWORD -> {
                openChangePassword()
            }
            Const.AccountAction.ACTION_LOGOUT -> {
                logout()
            }
            else -> {
            }
        }
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun openChangePassword() {
        context?.let {
            val intent = Intent(it, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openProfile() {
        context?.let {
            val intent = Intent(it, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(AccountViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.menu.observe(this, Observer { m ->
            m?.let {
                adapter.replaceAll(it)
            }
        })
        viewModel.loggedOut.observe(this, Observer { m ->
            m?.let {

            }
        })

        viewModel.loadMenu()
    }
}
