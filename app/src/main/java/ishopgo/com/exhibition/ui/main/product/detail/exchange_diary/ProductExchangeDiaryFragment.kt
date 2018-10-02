package ishopgo.com.exhibition.ui.main.product.detail.exchange_diary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExchangeDiaryProduct
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.ProductExchangeDiaryAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductExchangeDiaryFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var adapterExchangeDiary = ProductExchangeDiaryAdapter()
    private var data = ProductDetail()

    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_swipable_recyclerview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        setupDiaryProducts(view.context)
        swipe.setOnRefreshListener(this)
    }

    private fun setupDiaryProducts(context: Context) {
        if (data.exchangeDiaryProduct?.isEmpty() == true) {
            view_empty_result_notice.visibility = View.VISIBLE
            view_empty_result_notice.text = "Nội dung trống"
        } else view_empty_result_notice.visibility = View.GONE

        adapterExchangeDiary.replaceAll(data.exchangeDiaryProduct ?: mutableListOf())

        view_recyclerview.adapter = adapterExchangeDiary
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = true
        view_recyclerview.setHasFixedSize(false)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)

        adapterExchangeDiary.listener = object : ClickableAdapter.BaseAdapterAction<ExchangeDiaryProduct> {
            override fun click(position: Int, data: ExchangeDiaryProduct, code: Int) {
                when (code) {
                    DIARY_IMAGE_CLICK -> {
                        if (data.images?.isNotEmpty() == true) {
                            val listImage = ArrayList<String>()
                            for (i in data.images!!.indices)
                                listImage.add(data.images!![i].image ?: "")
                            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                            startActivity(intent)
                        } else toast("Không thể mở ảnh này")
                    }

                    DIARY_USER_CLICK -> {
                        val intent = Intent(context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setupToolbars() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.setCustomTitle("Nhật ký giao dịch")
    }
}