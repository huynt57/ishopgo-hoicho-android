package ishopgo.com.exhibition.ui.main.map


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.ExpoShop
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
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
                }
            }

        }
        return expoShopAdapter
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

    private var isSearchMode = false
    private var searchKeyword = ""

    private var expoId: Long = 0L
    private lateinit var expoInfo: ExpoConfig

    private val searchRunnable = Runnable {
        firstLoad()
    }

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

        view_search_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isSearchMode) return

                if (searchKeyword != s.toString()) {
                    searchKeyword = s.toString()
                    view_search_name.removeCallbacks(searchRunnable)
                    view_search_name.postDelayed(searchRunnable, 600)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        view_search_shop.setOnClickListener {
            view_search_name.visibility = if (isSearchMode) View.GONE else View.VISIBLE

            isSearchMode = !isSearchMode
            view_search_shop.setImageResource(if (isSearchMode) R.drawable.ic_close_default_24dp else R.drawable.ic_search_highlight_24dp)
            if (!isSearchMode) {
                if (searchKeyword.isNotBlank()) {
                    searchKeyword = ""
                    firstLoad()
                    view_search_name.setText("")
                }

                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view_search_name.windowToken, 0)
            } else {
                view_search_name.requestFocus()
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.showSoftInput(view_search_name, 0)
            }
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
        toolbar.setCustomTitle(if (::expoInfo.isInitialized) expoInfo.name
                ?: "" else "Sơ đồ hội chợ")
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
