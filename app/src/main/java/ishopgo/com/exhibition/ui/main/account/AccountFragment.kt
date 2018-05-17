package ishopgo.com.exhibition.ui.main.account

import android.annotation.SuppressLint
import android.app.Activity
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
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.account.password.ChangePasswordActivity
import ishopgo.com.exhibition.ui.main.boothmanager.BoothManagerActivity
import ishopgo.com.exhibition.ui.main.notification.NotificationActivity
import ishopgo.com.exhibition.ui.main.profile.ProfileActivity
import ishopgo.com.exhibition.ui.main.configbooth.ConfigBoothActivity
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerActivity
import ishopgo.com.exhibition.ui.main.report.ReportActivity
import ishopgo.com.exhibition.ui.main.salepoint.SalePointActivity
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {

    private val adapter = AccountMenuAdapter()
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.isEnabled = false
        if (UserDataManager.currentUserId > 0) {
            Glide.with(view.context)
                    .load(UserDataManager.currentUserAvatar)
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(view_avatar)
            view_name.text = UserDataManager.currentUserName
            view_introduce.text = "${UserDataManager.currentType} - ${UserDataManager.currentUserPhone}"

            view_recyclerview.adapter = adapter
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            view_recyclerview.layoutManager = layoutManager
//            view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing, true, false))

            view_profile_current.setOnClickListener {
                openProfile()
            }

            adapter.listener = object : ClickableAdapter.BaseAdapterAction<AccountMenuProvider> {
                override fun click(position: Int, data: AccountMenuProvider, code: Int) {
                    handleClick(data)
                }
            }
        } else {
            Glide.with(view.context)
                    .load(UserDataManager.currentUserAvatar)
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(view_avatar)
            view_name.text = "Bạn chưa đăng nhập"

            view_profile_current.setOnClickListener {
                val intent = Intent(context, LoginSelectOptionActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)
                activity?.finish()
            }

            view_introduce.visibility = View.GONE
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
            Const.AccountAction.ACTION_REPORT -> {
                openReportActivity()
            }
            Const.AccountAction.ACTION_LOGOUT -> {
                logout()
            }
            Const.AccountAction.ACTION_SETTING_BOTTH -> {
                openSettingBoothActivity()
            }
            Const.AccountAction.ACTION_NOTIFICATION -> {
                openNotificationActivity()
            }
            Const.AccountAction.ACTION_PRODUCT_MANAGER -> {
                openProductManager()
            }
            Const.AccountAction.ACTION_SALE_POINT -> {
                openSalePoint()
            }
            Const.AccountAction.ACTION_BOOTH_MANAGER -> {
                openBoothManager()
            }
            else -> {
                toast("Đang phát triển")
            }
        }
    }

    private fun openReportActivity() {
        context?.let {
            val intent = Intent(it, ReportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openBoothManager() {
        context?.let {
            val intent = Intent(it, BoothManagerActivity::class.java)
            startActivity(intent)
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

    private fun openSettingBoothActivity() {
        context?.let {
            val intent = Intent(it, ConfigBoothActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openNotificationActivity() {
        context?.let {
            val intent = Intent(it, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openProfile() {
        context?.let {
            val intent = Intent(it, ProfileActivity::class.java)
            startActivityForResult(intent, Const.RequestCode.UPDATE_PROFILE)
        }
    }

    private fun openProductManager() {
        context?.let {
            val intent = Intent(it, ProductManagerActivity::class.java)
            startActivityForResult(intent, Const.RequestCode.UPDATE_PROFILE)
        }
    }

    private fun openSalePoint() {
        context?.let {
            val intent = Intent(it, SalePointActivity::class.java)
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
                UserDataManager.deleteUserInfo()
                toast("Đăng xuất thành công")
                val intent = Intent(context, LoginSelectOptionActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)
                activity?.finish()
            }
        })

        viewModel.loadMenu()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.UPDATE_PROFILE && resultCode == Activity.RESULT_OK) {
            Glide.with(context)
                    .load(UserDataManager.currentUserAvatar)
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.error_placeholder))
                    .into(view_avatar)
            view_name.text = UserDataManager.currentUserName
        }
    }
}
