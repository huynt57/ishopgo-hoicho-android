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
import ishopgo.com.exhibition.domain.request.SearchShopRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListActionBarFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_expo_map.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpoMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExpoMapFragment : BaseListActionBarFragment<List<ExpoShopProvider>, ExpoShopProvider>() {

    override fun populateData(data: List<ExpoShopProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ExpoShopProvider> {
        val expoShopAdapter = ExpoShopAdapter()
        expoShopAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ExpoShopProvider> {
            override fun click(position: Int, data: ExpoShopProvider, code: Int) {

            }

        }
        return expoShopAdapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<ExpoShopProvider>> {
        return obtainViewModel(ExpoShopViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        Log.d(TAG, "search: $searchKeyword")
        val request = SearchShopRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.keyword = searchKeyword
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = SearchShopRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.keyword = searchKeyword
        viewModel.loadData(request)
    }

    private var isSearchMode = false
    private var searchKeyword = ""

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val searchRunnable = Runnable {
        firstLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
                }

                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view_search_name.windowToken, 0)
            }
            else {
                view_search_name.requestFocus()
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.showSoftInput(view_search_name, 0)
            }
        }
        view_zoom.setOnClickListener {
            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, arrayOf("http://www.visitsingapore.com/content/dam/MICE/Global/plan-your-event/venues/Singapore-EXPO/ExpoFE_Map_Jun3_2014-page-001.jpg"))
            startActivity(intent)
        }
        Glide.with(view.context)
                .load("http://www.visitsingapore.com/content/dam/MICE/Global/plan-your-event/venues/Singapore-EXPO/ExpoFE_Map_Jun3_2014-page-001.jpg")
                .apply(RequestOptions().centerCrop())
                .into(view_image)

    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Sơ đồ hội chợ")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpoMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ExpoMapFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

        private const val TAG = "ExpoMapFragment"
    }
}
