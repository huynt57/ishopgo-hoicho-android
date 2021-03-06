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
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.account.password.ChangePasswordActivity
import ishopgo.com.exhibition.ui.main.administrator.AdministratorActivity
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
import ishopgo.com.exhibition.ui.main.references.ReferencesActivity
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothActivity
import ishopgo.com.exhibition.ui.main.salepoint.SalePointActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.main.stamp.buystamp.BuyStampActivity
import ishopgo.com.exhibition.ui.main.stamp.listscanstamp.ListScanStampActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampActivity
import ishopgo.com.exhibition.ui.main.stamp.stampdistribution.StampDistributionActivity
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerActivity
import ishopgo.com.exhibition.ui.main.stamp.stampwarning.StampWarningActivity
import ishopgo.com.exhibition.ui.main.ticket.TicketActivity
import ishopgo.com.exhibition.ui.main.ticketmanager.TicketManagerActivity
import ishopgo.com.exhibition.ui.main.visitors.VisitorsActivity
import ishopgo.com.exhibition.ui.survey.SurveyActivity
import kotlinx.android.synthetic.main.fragment_account.*
import com.facebook.login.LoginManager
import com.facebook.AccessToken
import com.facebook.Profile


class AccountFragment : BaseFragment() {

    private val adapter = AccountMenuAdapter()
    private val adapterAdministrator = AccountMenuAdapter()
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
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

        if (UserDataManager.currentType == "Quản trị viên" || UserDataManager.currentType == "Nhân viên gian hàng") {
            view_menu_administrator.visibility = View.VISIBLE
            if (UserDataManager.currentType == "Quản trị viên")
                view_menu_administrator.setLabel("Quản trị viên")
            else view_menu_administrator.setLabel("Quản trị viên gian hàng")


            val listAdministrator = view_menu_administrator.getList()
            val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            layoutManager.isAutoMeasureEnabled = true
            listAdministrator.layoutManager = layoutManager
            listAdministrator.isNestedScrollingEnabled = false
            listAdministrator.adapter = adapterAdministrator

            adapterAdministrator.listener = object : ClickableAdapter.BaseAdapterAction<AccountMenuItem> {
                override fun click(position: Int, data: AccountMenuItem, code: Int) {
                    handleClick(data)
                }
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
            Const.AccountAction.ACTION_REFERENCES -> {
                openReferences()
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
            Const.AccountAction.ACTION_ADMINISTRATOR -> {
                openAdminstrator()
            }

            Const.AccountAction.ACTION_STAMP_MANAGER -> {
                openStampManager()
            }

            Const.AccountAction.ACTION_NO_STAMP -> {
                openNoStamp()
            }

            Const.AccountAction.ACTION_DISTRICBUTION_STAMP -> {
                openStampDistribution()
            }

            Const.AccountAction.ACTION_BUY_STAMP -> {
                openStampOrders()
            }

            Const.AccountAction.ACTION_LIST_SCAN_STAMP -> {
                openListScanStamp()
            }

            Const.AccountAction.ACTION_STAMP_WARNING -> {
                openListStampWarning()
            }

            Const.AccountAction.ACTION_CREATE_STAMP -> {
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

    private fun openReferences() {
        context?.let {
            val intent = Intent(it, ReferencesActivity::class.java)
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
        showProgressDialog()
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

    private fun openAdminstrator() {
        context?.let {
            val intent = Intent(it, AdministratorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openStampManager() {
        context?.let {
            val intent = Intent(it, StampManagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openNoStamp() {
        context?.let {
            val intent = Intent(it, NoStampActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openListStampWarning() {
        context?.let {
            val intent = Intent(it, StampWarningActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openListScanStamp() {
        context?.let {
            val intent = Intent(it, ListScanStampActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openStampDistribution() {
        context?.let {
            val intent = Intent(it, StampDistributionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openStampOrders() {
        context?.let {
            val intent = Intent(it, BuyStampActivity::class.java)
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

        viewModel.menuAdministrator.observe(this, Observer { m ->
            m?.let {
                adapterAdministrator.replaceAll(it)
            }
        })
        viewModel.loggedOut.observe(this, Observer { m ->
            m?.let {
                hideProgressDialog()
                UserDataManager.deleteUserInfo()
                toast("Đăng xuất thành công")
                if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
                    LoginManager.getInstance().logOut()
                }
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
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
