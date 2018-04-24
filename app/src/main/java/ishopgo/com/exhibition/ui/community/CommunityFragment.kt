package ishopgo.com.exhibition.ui.community

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.R.id.rv_community
import ishopgo.com.exhibition.domain.request.LoadMoreRequestParams
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_community.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityFragment : BaseActionBarFragment() {
    private val adapter = CommunityAdapter()
    private lateinit var viewModel: CommunityViewModel

    companion object {

        fun newInstance(params: Bundle): CommunityFragment {
            val fragment = CommunityFragment()
            fragment.arguments = params

            return fragment
        }

        const val COMMUNITY_SHARE_CLICK = 1
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_community
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_community.adapter = adapter
        rv_community.setHasFixedSize(true)
        rv_community.layoutManager = layoutManager
        rv_community.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                swipe.isRefreshing = true
                reloadData = false
                loadMore(totalItemsCount)
            }
        })

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<CommunityProvider> {
            override fun click(position: Int, data: CommunityProvider, code: Int) {
                when (code) {
                    COMMUNITY_SHARE_CLICK -> {
                        toast("Chia sẻ với cộng đồng")
                    }

                    COMMUNITY_LIKE_CLICK -> {
                        toast("Vào xem chi tiết sản phẩm")
                    }

                    COMMUNITY_COMMENT_CLICK -> {
                        toast("Vào xem chi tiết sản phẩm")
                    }

                    COMMUNITY_SHARE_NUMBER_CLICK -> {
                        toast("Vào xem chi tiết sản phẩm")
                    }

                    COMMUNITY_SHARE_PRODUCT_CLICK -> {
                        context?.let {
                            val dialog = MaterialDialog.Builder(it)
                                    .customView(R.layout.dialog_community_share, false)
                                    .autoDismiss(false)
                                    .canceledOnTouchOutside(true)
                                    .build()

                            val tv_new_post = dialog.findViewById(R.id.tv_new_post) as TextView
                            val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as TextView
                            val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as TextView

                            val postDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_write) }
                            tv_new_post.setCompoundDrawablesWithIntrinsicBounds(postDrawable, null, null, null)
                            tv_new_post.compoundDrawablePadding = 20

                            val fbDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_facebook) }
                            tv_share_facebook.setCompoundDrawablesWithIntrinsicBounds(fbDrawable, null, null, null)
                            tv_share_facebook.compoundDrawablePadding = 20

                            val zaloDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_zalo) }
                            tv_share_zalo.setCompoundDrawablesWithIntrinsicBounds(zaloDrawable, null, null, null)
                            tv_share_zalo.compoundDrawablePadding = 20

                            dialog.show()
                        }
                    }

                    COMMUNITY_PRODUCT_CLICK -> {
                        toast("Vào xem chi tiết sản phẩm")
                    }
                }
            }
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cộng đồng")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(CommunityViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataReturned.observe(this, Observer { data ->
            data?.let {
                populateData(it)
                swipe.isRefreshing = false
            }
        })

        reloadData = true
        firstLoad()
        swipe.isRefreshing = true
    }

    fun populateData(data: List<CommunityProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else {
            adapter.addAll(data)
        }
    }

    fun firstLoad() {
        val firstLoad = LoadMoreRequestParams()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    fun loadMore(currentCount: Int) {
        val loadMore = LoadMoreRequestParams()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }
}