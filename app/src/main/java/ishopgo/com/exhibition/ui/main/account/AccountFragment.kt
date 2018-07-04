package ishopgo.com.exhibition.ui.main.account

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.account.password.ChangePasswordActivity
import ishopgo.com.exhibition.ui.main.boothfollow.BoothFollowActivity
import ishopgo.com.exhibition.ui.main.boothmanager.BoothManagerActivity
import ishopgo.com.exhibition.ui.main.brandmanager.BrandManagerActivity
import ishopgo.com.exhibition.ui.main.map.config.ExpoMapConfigActivity
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerActivity
import ishopgo.com.exhibition.ui.main.myqr.MyQrActivity
import ishopgo.com.exhibition.ui.main.notification.NotificationActivity
import ishopgo.com.exhibition.ui.main.postmanager.PostManagerActivity
import ishopgo.com.exhibition.ui.main.productfollow.ProductFollowActivity
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerActivity
import ishopgo.com.exhibition.ui.main.profile.ProfileActivity
import ishopgo.com.exhibition.ui.main.questmanager.QuestionManagerActivity
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothActivity
import ishopgo.com.exhibition.ui.main.salepoint.SalePointActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.main.ticket.TicketActivity
import ishopgo.com.exhibition.ui.main.ticketmanager.TicketManagerActivity
import ishopgo.com.exhibition.ui.main.visitors.VisitorsActivity
import ishopgo.com.exhibition.ui.survey.SurveyActivity
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
//        swipe.isEnabled = false
        if (UserDataManager.currentUserId > 0) {
            createMenuForLoggedInUser(view)
        } else {
            createMenuForRequestLogin(view)
        }
    }

    private fun createMenuForRequestLogin(view: View) {
        Glide.with(view.context)
                .load(UserDataManager.currentUserAvatar)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .into(view_avatar)
        view_name.text = "Bạn chưa đăng nhập"

        view_profile_current.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
            activity?.finish()
        }

        view_introduce.visibility = View.GONE
        view_menu.visibility = View.GONE
    }

    private fun createMenuForLoggedInUser(view: View) {
        Glide.with(view.context)
                .load(UserDataManager.currentUserAvatar)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .into(view_avatar)
        view_name.text = UserDataManager.currentUserName
        view_introduce.text = "${UserDataManager.currentType} - ${UserDataManager.currentUserPhone}"

        view_menu.visibility = View.VISIBLE
        view_menu.setLabel("Thành viên")
        val list = view_menu.getList()
        val lm = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        lm.isAutoMeasureEnabled = true
        list.layoutManager = lm
        list.isNestedScrollingEnabled = false
        list.adapter = adapter

        view_profile_current.setOnClickListener {
            openProfile()
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<AccountMenuItem> {
            override fun click(position: Int, data: AccountMenuItem, code: Int) {
                handleClick(data)
            }
        }
    }

    private fun handleClick(data: AccountMenuItem) {
        val action = data.action
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
            Const.AccountAction.ACTION_MY_QR -> {
                openMyQrActivity()
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
            Const.AccountAction.ACTION_MEMBER_MANAGER -> {
                openMemberManager()
            }
            Const.AccountAction.ACTION_BRAND_MANAGER -> {
                openBrandManager()
            }
            Const.AccountAction.ACTION_NEWS_MANAGER -> {
                openPostManager(Const.AccountAction.ACTION_NEWS_MANAGER)
            }
            Const.AccountAction.ACTION_GENEREL_MANAGER -> {
                openPostManager(Const.AccountAction.ACTION_GENEREL_MANAGER)
            }
            Const.AccountAction.ACTION_QUESTION_MANAGER -> {
                openQuestionManager()
            }
            Const.AccountAction.ACTION_SYNTHETIC_MANAGER -> {
            }
            Const.AccountAction.ACTION_REGISTER_BOOTH -> {
                openRegisterBooth()
            }
            Const.AccountAction.ACTION_SURVEY -> {
                openSurvey()
            }
            Const.AccountAction.ACTION_TICKET -> {
                openTicket()
            }
            Const.AccountAction.ACTION_FAVORITE_PRODUCTS -> {
                openProductFavorite()
            }

            Const.AccountAction.ACTION_FAVORITE_BOOTHS -> {
                openBoothFavorite()
            }

            Const.AccountAction.ACTION_VISITORS -> {
                openVisitors()
            }

            Const.AccountAction.ACTION_TICKET_MANAGER -> {
                openTicketManager()
            }

            Const.AccountAction.ACTION_CONFIG_EXPO -> {
                openConfigExpo()
            }
            else -> {
                toast("Đang phát triển")
            }
        }
    }

    private fun openReportActivity() {
        context?.let {
            //            val intent = Intent(it, ReportActivity::class.java)
//            startActivity(intent)

            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/ishopgovn/"))

            if (i.resolveActivity(it.packageManager) != null) {
                it.startActivity(i)
            }
        }
    }

    private fun openBoothManager() {
        context?.let {
            val intent = Intent(it, BoothManagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openBrandManager() {
        context?.let {
            val intent = Intent(it, BrandManagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPostManager(typeManager: Int) {
        context?.let {
            val intent = Intent(it, PostManagerActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, typeManager)
            startActivity(intent)
        }
    }

    private fun openQuestionManager() {
        context?.let {
            val intent = Intent(it, QuestionManagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openRegisterBooth() {
        context?.let {
            val intent = Intent(it, RegisterBoothActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openSurvey() {
        context?.let {
            val intent = Intent(it, SurveyActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, Const.TransferKey.EXTRA_REQUIRE)
            startActivity(intent)
        }
    }

    private fun openTicket() {
        context?.let {
            val intent = Intent(it, TicketActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openProductFavorite() {
        context?.let {
            val intent = Intent(it, ProductFollowActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openVisitors() {
        context?.let {
            val intent = Intent(it, VisitorsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openBoothFavorite() {
        context?.let {
            val intent = Intent(it, BoothFollowActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openMemberManager() {
        context?.let {
            val intent = Intent(it, MemberManagerActivity::class.java)
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
            val boothId = UserDataManager.currentUserId // id gian hang chinh la id cua chu gian hang
            val intent = Intent(context, ShopDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
            startActivity(intent)
//            val intent = Intent(it, ConfigBoothActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun openMyQrActivity() {
        context?.let {
            val intent = Intent(it, MyQrActivity::class.java)
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

    private fun openConfigExpo() {
        context?.let {
            val intent = Intent(it, ExpoMapConfigActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openTicketManager() {
        context?.let {
            val intent = Intent(it, TicketManagerActivity::class.java)
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
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)
                activity?.finish()
                activity?.finishAffinity()
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
