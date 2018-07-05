package ishopgo.com.exhibition.ui.main.map


import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.Kiosk
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
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
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_expo_map.*


/**
 * A simple [Fragment] subclass.
 * Use the [ExpoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExpoDetailFragment : BaseListActionBarFragment<List<Kiosk>, Kiosk>() {

    override fun populateData(data: List<Kiosk>) {
        if (reloadData) {
            adapter.replaceAll(data)

            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Chưa có gian hàng nào"
            }
            else
                view_empty_result_notice.visibility = View.GONE
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Kiosk> {
        val expoShopAdapter = ExpoShopAdapter()
        expoShopAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Kiosk> {
            override fun click(position: Int, data: Kiosk, code: Int) {
                if (data.boothId != null && data.boothId != 0L) {
                    openShopDetail(data.boothId!!)
                } else {
                    chooseKiosk(data)
                }
            }

        }
        return expoShopAdapter
    }

    private fun chooseKiosk(data: Kiosk) {
        if (UserDataManager.currentUserId > 0) {
            if (UserDataManager.currentType == "Chủ hội chợ") {
                val extra = Bundle()
                extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(expoInfo))
                extra.putLong(Const.TransferKey.EXTRA_ID, data.id ?: -1L)
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoDetailFragment_to_chooseBoothFragment, extra)
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoDetailFragment_to_registerBoothFragmentActionBar)
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

    override fun obtainViewModel(): BaseListViewModel<List<Kiosk>> {
        return obtainViewModel(ExpoDetailViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        if (::expoInfo.isInitialized) {
            val request = ExpoShopLocationRequest()
            request.limit = Const.PAGE_LIMIT
            request.offset = 0
            request.searchKeyword = searchKeyword
            request.expoId = expoInfo.id!!
            viewModel.loadData(request)
        }
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        if (::expoInfo.isInitialized) {
            val request = ExpoShopLocationRequest()
            request.limit = Const.PAGE_LIMIT
            request.offset = currentCount
            request.searchKeyword = searchKeyword
            request.expoId = expoInfo.id!!
            viewModel.loadData(request)
        }
    }

    private var searchKeyword = ""

    private lateinit var expoInfo: ExpoConfig
    private val ticketObserver = Observer<Ticket> { t ->
        t?.let {
            hideProgressDialog()

            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoDetailFragment_to_ticketDetailFragmentActionBar2, extra)
        }
    }

    private val expoDetailObserver = Observer<ExpoConfig> {
        it?.let {
            expoInfo = it

            setupWithConfig()

            firstLoad()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is ExpoDetailViewModel) {
            val expoDetailViewModel = viewModel as ExpoDetailViewModel
            expoDetailViewModel.expoDetail.observe(this, expoDetailObserver)
            expoDetailViewModel.ticket.observe(this, ticketObserver)
        }

        val startWithId = requireActivity().intent.hasExtra(Const.TransferKey.EXTRA_ID)
        val startWithJson = requireActivity().intent.hasExtra(Const.TransferKey.EXTRA_JSON)

        when {
            startWithId -> {
                val expoId = requireActivity().intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
                startWithId(expoId)
            }
            startWithJson -> {
                val json = requireActivity().intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
                json?.let {
                    expoInfo = Toolbox.gson.fromJson(it, ExpoConfig::class.java)
                }

                requireNotNull(expoInfo)

                setupWithConfig()

                firstLoad()
            }
        }
    }

    private fun setupWithConfig() {
        view_introduce.setOnClickListener {
            val intent = Intent(context, FullDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, expoInfo.description)
            startActivity(intent)
        }

        view_get_ticket.setOnClickListener {
            showProgressDialog()
            if (viewModel is ExpoDetailViewModel) {
                val expoDetailViewModel = viewModel as ExpoDetailViewModel
                expoDetailViewModel.getTicket(expoInfo.id!!)
            }
        }

        view_zoom.setOnClickListener {
            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, arrayOf(expoInfo.map ?: ""))
            intent.putExtra(Const.TransferKey.EXTRA_FETCH_FULL_SIZE, true)
            startActivity(intent)
        }

        Glide.with(requireContext())
                .load(expoInfo.map ?: "")
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_image)

    }

    private fun startWithId(fairId: Long) {
        if (viewModel is ExpoDetailViewModel) {
            (viewModel as ExpoDetailViewModel).loadExpoDetail(fairId)
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_expo_map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Tìm kiếm gian hàng")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(R.color.md_grey_700.asColor(requireContext()))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            if (::expoInfo.isInitialized) {
                val extra = Bundle()
                extra.putLong(Const.TransferKey.EXTRA_ID, expoInfo.id!!)
                Navigation.findNavController(it).navigate(R.id.action_expoDetailFragment_to_searchBoothFragment, extra)
            }
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ExpoDetailFragment()

        private const val TAG = "ExpoDetailFragment"
    }
}
