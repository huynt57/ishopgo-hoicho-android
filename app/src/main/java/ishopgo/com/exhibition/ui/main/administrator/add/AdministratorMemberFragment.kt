package ishopgo.com.exhibition.ui.main.administrator.add

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.MemberRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerAdapter
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class AdministratorMemberFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_administrator_member
    }

    private lateinit var viewModel: MemberManagerViewModel
    private lateinit var memberViewModel: FragmentAdministratorViewModel
    private var phone = ""
    private var name = ""
    private var region = ""
    private val adapterRegion = RegionAdapter()
    private val adapter = MemberManagerAdapter()

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

        val firstLoad = MemberRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.start_time = ""
        firstLoad.end_time = ""
        firstLoad.phone = phone
        firstLoad.name = name
        firstLoad.region = region
        viewModel.loadData(firstLoad)
    }

    fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = MemberRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.start_time = ""
        loadMore.end_time = ""
        loadMore.phone = phone
        loadMore.name = name
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()

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
        viewModel = obtainViewModel(MemberManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.loadRegion.observe(this, Observer {
            it?.let { it1 -> adapterRegion.replaceAll(it1) }
        })

        viewModel.dataReturned.observe(this, Observer { p ->
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
        viewModel.loadRegion()
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
                    .customView(R.layout.dialog_search_member, false)
                    .positiveText("Tìm")
                    .onPositive { dialog, _ ->
                        val edit_member_phone = dialog.findViewById(R.id.edit_member_phone) as TextInputEditText
                        phone = edit_member_phone.text.toString().trim { it <= ' ' }

                        val edit_member_name = dialog.findViewById(R.id.edit_member_name) as TextInputEditText
                        name = edit_member_name.text.toString().trim { it <= ' ' }

                        val edit_member_region = dialog.findViewById(R.id.edit_member_region) as TextInputEditText
                        region = edit_member_region.text.toString()

                        firstLoad()
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_member_phone = dialog.findViewById(R.id.edit_member_phone) as TextInputEditText
            edit_member_phone.setText(phone)

            val edit_member_name = dialog.findViewById(R.id.edit_member_name) as TextInputEditText
            edit_member_name.setText(name)

            val edit_member_region = dialog.findViewById(R.id.edit_member_region) as TextInputEditText
            edit_member_region.setOnClickListener { getRegion(edit_member_region) }
            edit_member_region.setText(region)

            dialog.show()
        }
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn khu vực")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }
}