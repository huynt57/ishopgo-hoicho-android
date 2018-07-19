package ishopgo.com.exhibition.ui.main.administrator.add

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchMemberAdministratorRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.administrator.AdministratorViewModel
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class AdministratorMemberFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_administrator_member
    }

    private lateinit var viewModel: AdministratorViewModel
    private lateinit var memberViewModel: FragmentAdministratorViewModel
    private var name = ""
    private val adapter = MemberManagerAdapter()
    private var boothId: Long = -1L

    companion object {
        const val TAG = "AdministratorMemberFragment"

        fun newInstance(params: Bundle): AdministratorMemberFragment {
            val fragment = AdministratorMemberFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    fun firstLoad() {
        reloadData = true

        val firstLoad = SearchMemberAdministratorRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = name
        firstLoad.boothId = boothId
        viewModel.getMember(firstLoad)
    }

    fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = SearchMemberAdministratorRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.name = name
        loadMore.boothId = boothId
        viewModel.getMember(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()

        if (UserDataManager.currentType == "Chủ gian hàng")
            boothId = UserDataManager.currentUserId

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.adapter = adapter
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        })

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<MemberManager> {
            @SuppressLint("SetTextI18n")
            override fun click(position: Int, data: MemberManager, code: Int) {
                context?.let {
                    memberViewModel.getDataMember(data)
                    activity?.onBackPressed()
                }
            }
        }

        swipe.setOnRefreshListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        memberViewModel = obtainViewModel(FragmentAdministratorViewModel::class.java, true)
        viewModel = obtainViewModel(AdministratorViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.getDataMember.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

                    adapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else {
                    adapter.addAll(it)
                }
                swipe.isRefreshing = false
            }
        })

        reloadData = true
        swipe.isRefreshing = true
        firstLoad()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }

        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener { performFilter() }
    }

    private fun performFilter() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Tìm kiếm")
                    .customView(R.layout.dialog_search_member_administrator, false)
                    .positiveText("Tìm")
                    .onPositive { dialog, _ ->
                        val edit_member_name = dialog.findViewById(R.id.edit_member_name) as TextInputEditText
                        name = edit_member_name.text.toString().trim { it <= ' ' }

                        firstLoad()
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val edit_member_name = dialog.findViewById(R.id.edit_member_name) as TextInputEditText
            edit_member_name.setText(name)

            dialog.show()
        }
    }
}