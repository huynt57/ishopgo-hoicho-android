package ishopgo.com.exhibition.ui.main.configbooth

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.BoothRelate
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_booth_related.*

class BoothRelateFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "BoothRelateFragment"

        fun newInstance(params: Bundle): BoothRelateFragment {
            val fragment = BoothRelateFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onRefresh() {
        firstLoad()
    }

    private val adapter = BoothRelateAdapter()
    private lateinit var viewModel: ConfigBoothViewModel
    private lateinit var shareViewModel: ShareBoothViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_booth_related
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relativeLayout.setOnClickListener(null)
        setupToolbars()
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.adapter = adapter

        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        })

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
            override fun click(position: Int, data: BoothManager, code: Int) {
                if (checkRequireFields(edit_function)) {
                    val relate = BoothRelate()
                    relate.id = data.id
                    relate.name = data.boothName
                    relate.address = "${data.address},${data.region}"
                    relate.hotline = data.hotline
                    relate.content = edit_function.text.toString()

                    shareViewModel.getDataBoothRelated(relate)
                    activity?.onBackPressed()
                }
            }
        }

        swipe.setOnRefreshListener(this)
    }

    private fun checkRequireFields(view: TextInputEditText): Boolean {
        if (view.text.trim().isEmpty()) {
            toast("Chức năng của đơn vị không được để trống")
            view.error = "Trường này còn trống"
            return false
        }
        return true
    }

    fun firstLoad() {
        reloadData = true

        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadRelateBooths(firstLoad)
    }

    fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadRelateBooths(loadMore)
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chọn đơn vị liên quan")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }

        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener { }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shareViewModel = obtainViewModel(ShareBoothViewModel::class.java, true)

        viewModel = obtainViewModel(ConfigBoothViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.relateBooths.observe(this, Observer { p ->
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
}