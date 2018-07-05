package ishopgo.com.exhibition.ui.main.map


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.ExpoShop
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asColor
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_expo_map.*


/**
 * A simple [Fragment] subclass.
 * Use the [ExpoMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExpoMapFragment : BaseListActionBarFragment<List<ExpoShop>, ExpoShop>() {

    override fun populateData(data: List<ExpoShop>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ExpoShop> {
        val expoShopAdapter = ExpoShopAdapter()
        expoShopAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ExpoShop> {
            override fun click(position: Int, data: ExpoShop, code: Int) {
                if (data.boothId != null && data.boothId != 0L) {
                    openShopDetail(data.boothId!!)
                } else {
                    if (UserDataManager.currentType == "Quản trị viên") {
                        val listPermission = Toolbox.gson.fromJson<ArrayList<String>>(UserDataManager.listPermission, object : TypeToken<ArrayList<String>>() {}.type)

                        if (listPermission.isNotEmpty()) {
                            for (i in listPermission.indices)
                                if (Const.Permission.EXPO_MAP_ADD == listPermission[i]) {
                                    chooseShop(data)
                                } else toast("Bạn không có quyền để thêm gian hàng")
                        }
                    } else
                        chooseShop(data)
                }
            }

        }
        return expoShopAdapter
    }

    private fun chooseShop(data: ExpoShop) {
        if (UserDataManager.currentUserId > 0) {
            if (UserDataManager.currentType == "Chủ hội chợ") {
                val extra = Bundle()
                extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(expoInfo))
                extra.putLong(Const.TransferKey.EXTRA_ID, data.id ?: -1L)
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoMapFragment_to_chooseBoothFragment, extra)
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoMapFragment_to_registerBoothFragmentActionBar)
            }
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
        }
    }

    private fun openShopDetail(shopId: Long) {
        val intent = Intent(context, ShopDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, shopId)
        startActivity(intent)
    }

    override fun obtainViewModel(): BaseListViewModel<List<ExpoShop>> {
        return obtainViewModel(ExpoShopViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        Log.d(TAG, "search: $searchKeyword")
        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.searchKeyword = searchKeyword
        request.expoId = expoId
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.searchKeyword = searchKeyword
        request.expoId = expoId
        viewModel.loadData(request)
    }

    private var searchKeyword = ""

    private var expoId: Long = 0L
    private lateinit var expoInfo: ExpoConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        expoId = requireActivity().intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
        val json = requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
        json?.let {
            expoInfo = Toolbox.gson.fromJson(it, ExpoConfig::class.java)
        }

        requireNotNull(expoInfo)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_expo_map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        val json = activity?.intent?.getStringExtra(Const.TransferKey.EXTRA_JSON)
        val expoConfig = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
        view_introduce.setOnClickListener {
            val intent = Intent(context, FullDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, expoConfig.description)
            startActivity(intent)
        }

        view_zoom.setOnClickListener {
            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, arrayOf(expoInfo.map ?: ""))
            intent.putExtra(Const.TransferKey.EXTRA_FETCH_FULL_SIZE, true)
            startActivity(intent)
        }
        Glide.with(view.context)
                .load(expoInfo.map ?: "")
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_image)

    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Tìm kiếm gian hàng")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(R.color.md_grey_700.asColor(requireContext()))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(expoInfo))
            Navigation.findNavController(it).navigate(R.id.action_expoMapFragment_to_searchBoothFragment, extra)
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ExpoMapFragment()

        private const val TAG = "ExpoMapFragment"
    }
}
