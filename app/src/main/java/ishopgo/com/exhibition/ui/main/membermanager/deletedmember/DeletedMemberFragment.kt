package ishopgo.com.exhibition.ui.main.membermanager.deletedmember

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.DeletedMemberRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerProvider
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerViewModel
import ishopgo.com.exhibition.ui.widget.DateInputEditText
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class DeletedMemberFragment : BaseListFragment<List<MemberManagerProvider>, MemberManagerProvider>() {
    private var phone = ""
    private var name = ""
    private var region = ""
    private val adapterRegion = RegionAdapter()

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<MemberManagerProvider>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = DeletedMemberRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.start_time = ""
        firstLoad.end_time = ""
        firstLoad.phone = phone
        firstLoad.name = name
        firstLoad.region = region
        firstLoad.deleted_at = true
        (viewModel as MemberManagerViewModel).loadDeletedMember(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = DeletedMemberRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.start_time = ""
        loadMore.end_time = ""
        loadMore.phone = phone
        loadMore.name = name
        loadMore.region = region
        loadMore.deleted_at = true
        (viewModel as MemberManagerViewModel).loadDeletedMember(loadMore)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<MemberManagerProvider> {
        val adapter = DeletedMemberAdapter()
        adapter.addData(MemberManager())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<MemberManagerProvider>> {
        return obtainViewModel(MemberManagerViewModel::class.java, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<MemberManagerProvider>) {
            (adapter as ClickableAdapter<MemberManagerProvider>).listener = object : ClickableAdapter.BaseAdapterAction<MemberManagerProvider> {
                @SuppressLint("SetTextI18n")
                override fun click(position: Int, data: MemberManagerProvider, code: Int) {
                    if (data is MemberManager) {
                        context?.let {
                            MaterialDialog.Builder(it)
                                    .content("Bạn có muốn khôi phục thành viên này không?")
                                    .positiveText("Có")
                                    .onPositive { _, _ ->
                                        if (viewModel is MemberManagerViewModel) (viewModel as MemberManagerViewModel).restoreMembers(data.id)
                                        showProgressDialog()
                                    }
                                    .negativeText("Không")
                                    .onNegative { dialog, _ -> dialog.dismiss() }
                                    .show()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel is MemberManagerViewModel) {
            (viewModel as MemberManagerViewModel).restoreSusscess.observe(this, Observer {
                toast("Khôi phục thành công")
                hideProgressDialog()
                activity?.setResult(RESULT_OK)
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
        const val TAG = "DeletedMemberFragment"
        fun newInstance(params: Bundle): DeletedMemberFragment {
            val fragment = DeletedMemberFragment()
            fragment.arguments = params

            return fragment
        }
    }
}