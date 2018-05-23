package ishopgo.com.exhibition.ui.chat.local

import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ChatFragment : BaseFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return false
    }

//    private lateinit var viewModel: FbPageViewModel
//
//    companion object {
//        val MODE_LOCAL = 1
//        val MODE_CHATBOX = 2
//        val MODE_IWEB = 3
//        val MODE_PAGE = 4
//
//        val TYPE_ALL = 1
//        val TYPE_UNREAD = 4
//        val TYPE_CARE = 5
//        val TYPE_IWEB = 6
//
//        fun newInstance(params: Bundle): ChatFragment {
//            val f = ChatFragment()
//            f.arguments = params
//
//            return f
//        }
//    }
//
//    private var mode: Int = 0
//    private var type: Int = TYPE_ALL
//
//    private var filterBy: Int = -1
//    private var searchKeyword: String? = null
//
//    private var mSectionsPagerAdapter: CountSpecificPager? = null
//    private lateinit var localChatAdapter: LocalPagerAdapter
//    private lateinit var chatBoxAdapter: ChatBoxPagerAdapter
//    private lateinit var commentWebAdapter: IwebPagerAdapter
//    private lateinit var fbpageAdapter: FbPagePagerAdapter
//
////    private lateinit var drawerToggle: ActionBarDrawerToggle
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_i_shop_page, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = obtainViewModel(FbPageViewModel::class.java, false)
//        viewModel.errorSignal.observe(this, Observer { it?.let { resolveError(it) } })
//
//        viewModel.listFanpageAll.observe(this, Observer { p ->
//            p?.let {
//                adapterAllFanpageAll.replaceAll(it)
//            }
//        })
//
//        viewModel.listFanpageNow.observe(this, Observer { p ->
//            p?.let {
//                val data = it
//                if (data != null && data.isNotEmpty()) {
//                    nav_view.menu.removeGroup(R.id.group_fanpage)
//                    for (i in data.indices) {
//                        nav_view.menu?.let {
//                            val newPageItem = it.add(R.id.group_fanpage, View.generateViewId(), it.size() + 1, data[i].name)
//                            newPageItem.icon = context?.let { it1 -> AppCompatResources.getDrawable(it1, R.drawable.com_facebook_favicon_blue) }
//                        }
//                    }
//                }
//            }
//        })
//
//        (activity as AppCompatActivity).title = "Chat nội bộ"
//
//        val user = UserDataManager.currentUser
//        Glide.with(this).load(user.image
//                ?: "").apply(RequestOptions().circleCrop().placeholder(R.drawable.avatar_placeholder)).into(nav_view.getHeaderView(0).findViewById(R.id.view_user_avatar))
//        nav_view.getHeaderView(0).findViewById<TextView>(R.id.view_system_name).text = user.name
//
////        viewModel.getAllFanpageExistent()
////        viewModel.getAllFanpage()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//
//    private fun initToolbar() {
//        toolbar.showDivider(false)
//        toolbar.option1(R.drawable.ic_search_black_24dp)
//        toolbar.setOption1ClickListener(View.OnClickListener {
//            toolbar.showSearch(true)
//            toolbar.startButton(R.drawable.ic_close_black_24dp)
//            toolbar.setStartButtonClickListener(View.OnClickListener {
//                toolbar.showSearch(false)
//                // disable fanpage
//                toolbar.startButton(0)
////                toolbar.setStartButtonClickListener(View.OnClickListener {
////                    if (!drawer_layout.isDrawerVisible(GravityCompat.START))
////                        drawer_layout.openDrawer(GravityCompat.START)
////                    else
////                        drawer_layout.closeDrawer(GravityCompat.START)
////                })
//            })
//        })
//
////        val typePlatform = UserDataManager.currentTypePlatform
////        if (typePlatform == LoginResponse.PLATFORM_QUAN_LY_NHAN_SU) {
////            // platform hr do not support fanpage
////        } else {
////            toolbar.startButton(R.drawable.ic_chat_toggle)
////            toolbar.setStartButtonClickListener(View.OnClickListener {
////                if (!drawer_layout.isDrawerVisible(GravityCompat.START))
////                    drawer_layout.openDrawer(GravityCompat.START)
////                else
////                    drawer_layout.closeDrawer(GravityCompat.START)
////            })
////
////            drawer_layout.openDrawer(GravityCompat.START)
////        }
//
//
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//
//        toolbar.setSearchActionListener(object : AppToolbar.OnSearchActionListener {
//            override fun onSearchEnabled() {
//                Log.d(TAG, "onSearchEnabled")
//                searchKeyword = null
//            }
//
//            override fun onSearchDisabled() {
//                Log.d(TAG, "onSearchDisabled")
//                searchKeyword = null
//            }
//
//            override fun onSearch(query: String) {
//                Log.d(TAG, "onSearch $query")
//                searchKeyword = query
//
//                val fragment = mSectionsPagerAdapter?.getFragment(tabs_message.selectedTabPosition.toLong())
//                (fragment as? SearchableFilterableNewFragment)?.searchFor(searchKeyword)
//            }
//
//        })
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item?.itemId == R.id.action_settings) {
//            Toast.makeText(context, "Cài đặt, Ẩn toàn bộ comment, Ẩn toàn bộ comment có số điện thoại ", Toast.LENGTH_SHORT).show()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initToolbar()
//
//        nav_view.setNavigationItemSelectedListener(this)
//        val itemCurrentDomain = nav_view.menu.findItem(R.id.nav_current_domain)
//        itemCurrentDomain.title = UserDataManager.currentDomain
//        nav_view.setCheckedItem(R.id.nav_local_chat)
//        toolbar.setCustomTitle("Chat nội bộ")
//
//        val userAvatar = "https://scontent.fhan2-2.fna.fbcdn.net/v/t1.0-1/p160x160/21462924_1484641024917828_4756007823204461751_n.jpg?oh=10e63b108bf6ec26c6900247e5991043&oe=5A8E3C4E"
//        val view_avatar = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.view_user_avatar)
//        view_facebook_name = nav_view.getHeaderView(0).findViewById(R.id.view_facebook_name)
//        itemLogOut = nav_view.menu.findItem(R.id.nav_logout)
//
//        Glide.with(this)
//                .load(userAvatar)
//                .apply(RequestOptions.centerInsideTransform()
//                        .circleCrop()
//                        .placeholder(R.drawable.avatar_placeholder))
//                .into(view_avatar)
//
//        if (accessToken == null || accessToken.isExpired) {
//            view_facebook_name?.text = "Chưa đăng nhập"
//            view_facebook_name?.setOnClickListener({
//                loginFb(false)
//            })
//            itemLogOut?.isVisible = false
//        } else {
//            itemLogOut?.isVisible = true
//            if (Profile.getCurrentProfile() != null) {
//                view_facebook_name?.text = Profile.getCurrentProfile().name
//            }
//        }
//
//        fragmentManager?.let {
//            localChatAdapter = LocalPagerAdapter(it)
//            chatBoxAdapter = ChatBoxPagerAdapter(it)
//            commentWebAdapter = IwebPagerAdapter(it)
//            fbpageAdapter = FbPagePagerAdapter(it)
//        }
//
//        setListeners()
//
//        initFilters()
//
//        loadLocalChat()
//    }
//
//    private fun initFilters() {
//        searchKeyword = null
//        filterBy = -1
//    }
//
//    private fun setListeners() {
//        view_filter.setOnClickListener {
//            val fragment = mSectionsPagerAdapter?.getFragment(tabs_message.selectedTabPosition.toLong())
//            (fragment as? SearchableFilterableNewFragment)?.showFilterDialog()
//        }
//    }
//
//    override fun onBackPressed(): Boolean {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//            return true
//        } else {
//            return super.onBackPressed()
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        Log.d(TAG, "onNavigationItemSelected")
//        // Handle navigation view item clicks here.
//
//        when (item.itemId) {
//            R.id.nav_local_chat -> {
//                toolbar.setCustomTitle("Chat nội bộ")
//                loadLocalChat()
//            }
//            R.id.nav_current_domain -> {
//                toolbar.setCustomTitle("Chat box")
//                loadChatBox()
//            }
//            R.id.nav_add_source -> {
//                addFbSource()
//            }
//            R.id.nav_manage_task -> {
//                startActivity(Intent(activity, ManageTasksActivity::class.java))
//                return false
//            }
//            R.id.nav_logout -> {
//                LoginManager.getInstance().logOut()
//                AccessToken.setCurrentAccessToken(null)
//
//                itemLogOut?.isVisible = false
//                view_facebook_name?.text = "Chưa đăng nhập"
//                view_facebook_name?.setOnClickListener({
//                    loginFb(false)
//                })
//
////                viewModel.getAllFanpageExistent()
//            }
//            else -> {
//                toolbar.setCustomTitle(item.title)
//                item.isCheckable = true
//                loadFbPage(FanpageNow())
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//
//    }
//
//    private fun addFbSource() {
//        if (accessToken == null || accessToken.isExpired) {
//            loginFb(true)
//        } else {
//            drawer_layout.closeDrawer(GravityCompat.START, false)
//            showAllPages()
//        }
//    }
//
//    private fun showAllPages() {
//        context?.let {
//            val dialog = MaterialDialog.Builder(it)
//                    .title("Chọn trang muốn quản lý")
//                    .customView(R.layout.content_only_recyclerview, false)
//                    .positiveText("Hoàn tất")
//                    .negativeText("Huỷ")
//                    .onPositive { dialog, which ->
//                        dialog.apply {
//                            if (adapterAllFanpageAll.getListFanPageAll() != null) {
//                                Log.d(TAG, adapterAllFanpageAll.getListFanPageAll()!!.size.toString())
//                            }
//                        }
//                    }
//                    .onNegative { dialog, _ -> dialog.dismiss() }
//                    .build()
//
//            val rv = dialog.findViewById(R.id.recyclerview) as RecyclerView
//            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            rv.setHasFixedSize(true)
//            rv.adapter = adapterAllFanpageAll
//
//            val accessToken = AccessToken.getCurrentAccessToken()
//            accessToken?.apply {
//                Log.d(TAG, "acess token: $token")
//            }
//
//            val window = dialog.window
//            if (window != null) {
//                window.attributes.windowAnimations = R.style.BottomDialog
//                window.setGravity(Gravity.BOTTOM)
//            }
//
//            dialog.show()
//        }
//
//    }
//
//    private fun addPageToSource(data: FanpageAll) {
//        context?.let { ctx ->
//            run {
//                val menu = nav_view.menu
//                menu?.let {
//                    val newPageItem = it.add(R.id.group_managing, View.generateViewId(), it.size() + 1, data.name)
//                    newPageItem.icon = AppCompatResources.getDrawable(ctx, R.drawable.com_facebook_favicon_blue)
//                }
//            }
//        }
//    }
//
//    private fun isPageAdded(data: FanpageAll): Boolean {
//        return false
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun loadLocalChat() {
//        Log.d(TAG, "loadLocalChat")
//        val isChanged = mode != MODE_LOCAL
//        mode = MODE_LOCAL
//        type = TYPE_ALL
//        tabs_message.visibility = View.VISIBLE
//        if (isChanged) populateTabs()
//        activity?.invalidateOptionsMenu()
//    }
//
//    private fun loadChatBox() {
//        Log.d(TAG, "loadChatbox")
//        val isChanged = mode != MODE_CHATBOX
//        mode = MODE_CHATBOX
//        type = TYPE_ALL
//        tabs_message.visibility = View.VISIBLE
//        if (isChanged) populateTabs()
//        activity?.invalidateOptionsMenu()
//    }
//
//    private fun loadIwebChat() {
//        Log.d(TAG, "loadIwebChat")
//        val isChanged = mode != MODE_IWEB
//        tabs_message.visibility = View.VISIBLE
//        mode = MODE_IWEB
//        type = TYPE_ALL
//        if (isChanged) populateTabs()
//        activity?.invalidateOptionsMenu()
//    }
//
//    private fun loadFbPage(item: FanpageNow) {
//        Log.d(TAG, "loadFbPage")
//        val isChanged = mode != MODE_PAGE
//        tabs_message.visibility = View.VISIBLE
//        mode = MODE_PAGE
//        type = TYPE_ALL
//        if (isChanged) populateTabs()
//
//        activity?.invalidateOptionsMenu()
//
//
//    }
//
//    private fun populateTabs() {
//        tabs_message.removeAllTabs()
//        when (mode) {
//            MODE_LOCAL -> {
//                tabs_message.addTab(tabs_message.newTab().setText("Tin nhắn").setTag(TYPE_ALL))
//                tabs_message.addTab(tabs_message.newTab().setText("Danh bạ").setTag(TYPE_UNREAD))
//                tabs_message.addTab(tabs_message.newTab().setText("Lịch hẹn").setTag(TYPE_CARE))
//                mSectionsPagerAdapter = localChatAdapter
//            }
//            MODE_CHATBOX -> {
//                tabs_message.addTab(tabs_message.newTab().setText("Tin nhắn").setTag(TYPE_ALL))
//                tabs_message.addTab(tabs_message.newTab().setText("Bình luận web").setTag(TYPE_IWEB))
//                mSectionsPagerAdapter = chatBoxAdapter
//            }
//            MODE_IWEB -> {
//                tabs_message.addTab(tabs_message.newTab().setText("Tất cả").setTag(TYPE_ALL))
//                mSectionsPagerAdapter = commentWebAdapter
//            }
//            MODE_PAGE -> {
//                tabs_message.addTab(tabs_message.newTab().setText("Tất cả").setTag(TYPE_ALL))
//                tabs_message.addTab(tabs_message.newTab().setText("Lịch hẹn").setTag(TYPE_CARE))
//                mSectionsPagerAdapter = fbpageAdapter
//            }
//        }
//
//        mSectionsPagerAdapter?.let {
//            fragmentManager?.apply {
//                //                val transaction = beginTransaction()
////                for (i in 0..tabs_message.tabCount) {
////                    val fragment = mSectionsPagerAdapter?.getFragment(tabs_message.selectedTabPosition.toLong())
////                    fragment?.let { transaction.remove(fragment) }
////                }
////                transaction.commit()
////                noswipe_pager.removeAllViews()
////                noswipe_pager.adapter = null
//
//                noswipe_pager.setPagingEnable(true)
//                noswipe_pager.offscreenPageLimit = tabs_message.tabCount
//                noswipe_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs_message))
//                tabs_message.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(noswipe_pager))
//
//                noswipe_pager.adapter = it
//            }
//
//        }
//    }
//
//    private fun loginFb(showAllPage: Boolean) {
//        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onError(error: FacebookException?) {
//                Toast.makeText(context, "Có lỗi xảy ra. Hãy thử lại!", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCancel() {
//                Toast.makeText(context, "Hãy đăng nhập facebook để thêm trang", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onSuccess(result: LoginResult?) {
//                if (showAllPage) {
//                    showAllPages()
//                }
//                if (accessToken == null || accessToken.isExpired) {
//
//                } else {
//                    itemLogOut?.isVisible = true
//                    if (Profile.getCurrentProfile() != null) {
//                        view_facebook_name?.text = Profile.getCurrentProfile().name
//                    }
//                }
//            }
//        })
//        LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("public_profile"))
//    }
//
//    inner abstract class CountSpecificPager(fm: FragmentManager, tabCount: Int) : FragmentStatePagerAdapter(fm) {
//
//        private var tabCount: Int = 0
//        private var mFragmentManager: FragmentManager
//        private val mFragments = SparseArray<Fragment>()
//
//        init {
//            this.tabCount = tabCount
//            this.mFragmentManager = fm
//        }
//
//        override fun getCount(): Int {
//            // Show 3 total pages.
//            return tabCount
//        }
//
//        override fun getItemPosition(`object`: Any): Int {
//            return super.getItemPosition(`object`)
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            mFragments.remove(position)
//            super.destroyItem(container, position, `object`)
//        }
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            val fragment = super.instantiateItem(container, position) as Fragment
//            mFragments.put(position, fragment)
//            return fragment
//        }
//
//        fun getFragment(position: Long): Fragment? {
//            return mFragments.get(position.toInt())
//        }
//    }
//
//    inner class LocalPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 3) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> LocalInboxFragment()
//                1 -> ContactFragment()
//                2 -> LocalCareFragment()
//                else -> Fragment()
//            }
//        }
//    }
//
//    inner class ChatBoxPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 2) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> ChatboxFragment()
//                1 -> CommentWebFragment()
//                else -> Fragment()
//            }
//        }
//    }
//
//    inner class IwebPagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 1) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> CommentWebFragment()
////                1 -> InboxFragment()
////                2 -> InboxFragment()
//                else -> Fragment()
//            }
//        }
//    }
//
//    inner class FbPagePagerAdapter(fm: FragmentManager) : CountSpecificPager(fm, 2) {
//        override fun getItem(position: Int): Fragment {
//            return when (position) {
//                0 -> FbPageNewFragment()
//                1 -> FbPageCareFragment()
////                2 -> InboxFragment()
////                3 -> InboxFragment()
////                4 -> InboxFragment()
////                5 -> InboxFragment()
//                else -> Fragment()
//            }
//        }
//    }
}