package ishopgo.com.exhibition.ui.main.administrator

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.AdministratorRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.administrator.add.AdministratorAddActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class AdministratorFragment : BaseListFragment<List<Administrator>, Administrator>() {
    private var boothId: Long = -1L

    override fun initLoading() {
        firstLoad()
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<Administrator>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<Administrator> {
        val adapter = AdministratorAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Administrator> {
            @SuppressLint("ObsoleteSdkInt")
            override fun click(position: Int, data: Administrator, code: Int) {
                when (code) {
                    CLICK_MORE -> showSettings(data)
                    CLICK_USER_INFOR -> {
                        val intent = Intent(context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(intent)
                    }
                }
            }
        }
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<Administrator>> {
        return obtainViewModel(AdministratorViewModel::class.java, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (viewModel as AdministratorViewModel).deleteSusscess.observe(this, Observer {
            hideProgressDialog()
            toast("Xoá thành công")
            firstLoad()
        })
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = AdministratorRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.boothId = boothId
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = AdministratorRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.boothId = boothId
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (UserDataManager.currentType == "Chủ gian hàng")
            boothId = UserDataManager.currentUserId
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    private fun showSettings(administrator: Administrator) {
        val fragment = AdministratorBottomSheet.newInstance(Bundle())
        fragment.chooseEdit = View.OnClickListener { openEditAdministrator(administrator) }
        fragment.chooseCancel = View.OnClickListener { fragment.dismiss() }
        fragment.chooseDelete = View.OnClickListener { dialogDeleteAdministrator(administrator) }
        fragment.show(childFragmentManager, "AdministratorBottomSheet")
    }

    private fun dialogDeleteAdministrator(administrator: Administrator) {
        context?.let {
            MaterialDialog.Builder(it)
                    .content("Bạn có muốn xoá thành viên này không?")
                    .positiveText("Có")
                    .onPositive { _, _ ->
                        activity?.let {
                            (viewModel as AdministratorViewModel).deleteAdministrator(administrator.id)
                        }
                        showProgressDialog()
                    }
                    .negativeText("Không")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun openAddAdministrator() {
        val intent = Intent(context, AdministratorAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.ADMINISTRATOR_ADD)
    }

    @SuppressLint("SetTextI18n")
    private fun openEditAdministrator(account: Administrator) {
        val intent = Intent(context, AdministratorAddActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(account))
        startActivityForResult(intent, Const.RequestCode.ADMINISTRATOR_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.ADMINISTRATOR_ADD && resultCode == RESULT_OK)
            firstLoad()
    }

    companion object {
        const val TAG = "AdministratorFragment"
        fun newInstance(params: Bundle): AdministratorFragment {
            val fragment = AdministratorFragment()
            fragment.arguments = params

            return fragment
        }

        const val CLICK_MORE = 0
        const val CLICK_USER_INFOR = 1
    }
}