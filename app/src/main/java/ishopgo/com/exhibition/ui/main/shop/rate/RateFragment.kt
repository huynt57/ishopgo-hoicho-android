package ishopgo.com.exhibition.ui.main.shop.rate

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RatingBar
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ShopRatesRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_shop_rating.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class RateFragment : BaseListFragment<List<ShopRateProvider>, ShopRateProvider>() {

    companion object {
        fun newInstance(params: Bundle): RateFragment {
            val fragment = RateFragment()
            fragment.arguments = params
            return fragment
        }
    }

    private var shopId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun populateData(data: List<ShopRateProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ShopRateProvider> {
        return RateAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = ShopRatesRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.shopId = shopId
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ShopRatesRequest()
        val item = adapter.getItem(adapter.itemCount - 1)
        if (item is IdentityData) {
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.offset = currentCount
            loadMore.shopId = shopId
            viewModel.loadData(loadMore)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (UserDataManager.currentUserId > 0) showDialogRating(rating) else ratingBar.visibility=View.GONE
        }
    }

    private fun showDialogRating(rating: Float) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Đánh giá")
                    .customView(R.layout.dialog_add_rate, false)
                    .positiveText("Đăng")
                    .onPositive { dialog, _ ->
                        val edit_shop_rating = dialog.findViewById(R.id.edit_shop_rating) as TextInputEditText
                        val ratingBar_dialog = dialog.findViewById(R.id.ratingBar) as RatingBar
                        if (checkRequireFields(edit_shop_rating)) {
                            (viewModel as RateViewModel).createProductSalePoint(shopId, edit_shop_rating.text.toString(), ratingBar_dialog.numStars)
                            dialog.dismiss()
                        }
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ ->
                        dialog.dismiss()
                    }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val ratingBar_dialog = dialog.findViewById(R.id.ratingBar) as RatingBar
            ratingBar_dialog.rating = rating

            dialog.show()
        }
    }

    private fun checkRequireFields(view_content: TextInputEditText): Boolean {
        if (view_content.text.trim().isEmpty()) {
            toast("Nội dung không được để trống")
            view_content.error = getString(R.string.error_field_required)
            return false
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (viewModel as RateViewModel).createRateSusscess.observe(this, Observer {
            toast("Cảm ơn bạn đã đánh giá gian hàng")
            firstLoad()
        })
    }

    override fun obtainViewModel(): BaseListViewModel<List<ShopRateProvider>> {
        return obtainViewModel(RateViewModel::class.java, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_rating, container, false)
    }
}