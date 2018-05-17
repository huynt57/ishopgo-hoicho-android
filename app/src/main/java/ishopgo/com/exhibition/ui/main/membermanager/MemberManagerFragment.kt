package ishopgo.com.exhibition.ui.main.membermanager

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.MemberRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.widget.DateInputEditText
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class MemberManagerFragment : BaseListFragment<List<MemberManagerProvider>, MemberManagerProvider>() {
    private var startTime = ""
    private var endTime = ""
    private var phone = ""
    private var name = ""
    private var region = ""
    private val adapterRegion = RegionAdapter()

    override fun populateData(data: List<MemberManagerProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = MemberRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.start_time = startTime
        firstLoad.end_time = endTime
        firstLoad.phone = phone
        firstLoad.name = name
        firstLoad.region = region
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = MemberRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.start_time = startTime
        loadMore.end_time = endTime
        loadMore.phone = phone
        loadMore.name = name
        loadMore.region = region
        viewModel.loadData(loadMore)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<MemberManagerProvider> {
        val adapter = MemberManagerAdapter()
        adapter.addData(MemberManager())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<MemberManagerProvider>> {
        return obtainViewModel(MemberManagerViewModel::class.java, false)
    }

    fun openRestoreMember() {
        toast("Đang phát triển")
    }

    fun performFilter() {
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

                        val edit_member_start_time = dialog.findViewById(R.id.edit_member_start_time) as DateInputEditText
                        startTime = edit_member_start_time.text.toString()

                        val edit_member_end_time = dialog.findViewById(R.id.edit_member_end_time) as DateInputEditText
                        endTime = edit_member_end_time.text.toString()

                        val edit_member_region = dialog.findViewById(R.id.edit_member_region) as TextInputEditText
                        region = edit_member_region.text.toString()


                        if (edit_member_start_time.isError) {
                            toast("Ngày không hợp lệ")
                            return@onPositive
                        }

                        if (edit_member_end_time.isError) {
                            toast("Ngày không hợp lệ")
                            return@onPositive
                        }

                        firstLoad()
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_member_start_time = dialog.findViewById(R.id.edit_member_start_time) as DateInputEditText
            edit_member_start_time.setText(startTime)

            val edit_member_end_time = dialog.findViewById(R.id.edit_member_end_time) as DateInputEditText
            edit_member_end_time.setText(endTime)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<MemberManagerProvider>) {
            (adapter as ClickableAdapter<MemberManagerProvider>).listener = object : ClickableAdapter.BaseAdapterAction<MemberManagerProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: MemberManagerProvider, code: Int) {
                    if (data is MemberManager) {
                        if (viewModel is MemberManagerViewModel) (viewModel as MemberManagerViewModel).deleteMember(data.id)
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel is MemberManagerViewModel) {
            (viewModel as MemberManagerViewModel).deleteSusscess.observe(this, Observer {
                toast("Xoá thành công")
                firstLoad()
            })

            (viewModel as MemberManagerViewModel).loadRegion.observe(this, Observer {
                it?.let { it1 -> adapterRegion.replaceAll(it1) }
            })

            (viewModel as MemberManagerViewModel).loadRegion()

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

    companion object {
        const val TAG = "MemberManagerFragment"
        fun newInstance(params: Bundle): MemberManagerFragment {
            val fragment = MemberManagerFragment()
            fragment.arguments = params

            return fragment
        }
    }
}