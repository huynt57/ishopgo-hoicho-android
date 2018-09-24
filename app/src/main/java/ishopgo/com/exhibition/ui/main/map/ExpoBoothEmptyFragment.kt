package ishopgo.com.exhibition.ui.main.map

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.Kiosk
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import kotlinx.android.synthetic.main.empty_list_result.*

class ExpoBoothEmptyFragment : BaseListFragment<List<Kiosk>, Kiosk>() {
    private var expoInfo: ExpoConfig? = null
    private var fairId = -1L
    private lateinit var shareViewModel: ExpoMapShareViewModel

    companion object {
        fun newInstance(params: Bundle): ExpoBoothEmptyFragment {
            val f = ExpoBoothEmptyFragment()
            f.arguments = params
            return f
        }
    }

    override fun initLoading() {
        firstLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fairId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        expoInfo = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
    }

    override fun populateData(data: List<Kiosk>) {
        if (reloadData) {
            adapter.replaceAll(data)

            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Chưa có gian hàng nào"
            } else
                view_empty_result_notice.visibility = View.GONE
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Kiosk> {
        val expoShopAdapter = ExpoShopAdapter()
        expoShopAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Kiosk> {
            override fun click(position: Int, data: Kiosk, code: Int) {
                if (UserDataManager.currentType == "Quản trị viên") {
                    val listPermission = Const.listPermission

                    if (listPermission.isNotEmpty()) {
                        for (i in listPermission.indices)
                            if (Const.Permission.EXPO_MAP_ADD == listPermission[i]) {
                                chooseKiosk(data)
                            } else toast("Bạn không có quyền để thêm gian hàng")
                    }
                } else
                    chooseKiosk(data)
            }

        }
        return expoShopAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shareViewModel = obtainViewModel(ExpoMapShareViewModel::class.java, true)
        shareViewModel.addBoothSuccess.observe(this, Observer { firstLoad() })
    }

    private fun chooseKiosk(data: Kiosk) {
        if (UserDataManager.currentUserId > 0) {
            if (UserDataManager.currentType == "Chủ hội chợ") {
                shareViewModel.openBoothSelected(data.id ?: -1L)
//                val extra = Bundle()
//                extra.putLong(Const.TransferKey.EXTRA_ID, data.id ?: -1L)
//                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_chooseBoothFragment, extra)
            } else {
                shareViewModel.openRegisterExpo()
//                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_registerBoothFragmentActionBar)
            }
        } else {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<Kiosk>> {
        return obtainViewModel(ExpoDetailViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.searchKeyword = searchKeyword
        request.typeFilter = 2

        if (fairId != -1L)
            request.expoId = fairId
        else
            if (expoInfo != null)
                request.expoId = expoInfo?.id ?: -1L
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.searchKeyword = searchKeyword
        request.typeFilter = 2

        if (fairId != -1L)
            request.expoId = fairId
        else
            if (expoInfo != null)
                request.expoId = expoInfo?.id ?: -1L
        viewModel.loadData(request)
    }

    private var searchKeyword = ""
}