package ishopgo.com.exhibition.ui.main.product.detail

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.LinearLayout
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.ProductInfo
import kotlinx.android.synthetic.main.fragment_product_detail.view.*

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
class CustomProductDetail : ProductDetailOverwrite() {

    override fun handleInOtherFlavor(rootView: View, detail: ProductDetail) {
        rootView.apply {
            val listInfo = detail.info ?: mutableListOf()
            val linearLayout = view_content_container as LinearLayout

            for (i in listInfo.indices) {
                val productInfo = ProductInfo(rootView.context)
                productInfo.apply {
                    label_products_same_shop.text = listInfo[i].name
                    val adapter = ProductAdapter(0.4f)
                    if (listInfo[i].products?.data?.isNotEmpty() == true) {
                        var count = 0

                        listInfo[i].products?.data?.let { adapter.replaceAll(it) }

                        view_list_products_same_shop.adapter = adapter
                        val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                        layoutManager.isAutoMeasureEnabled = true
                        view_list_products_same_shop.layoutManager = layoutManager
                        view_list_products_same_shop.isNestedScrollingEnabled = false
                        view_list_products_same_shop.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
                        more_products_same_shop.visibility = View.GONE

                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                            override fun click(position: Int, data: Product, code: Int) {
                                context?.let {
                                    val intent = Intent(context, ProductDetailActivity::class.java)
                                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                                    it.startActivity(intent)
                                }
                            }
                        }

                        linearLayout.addView(productInfo, 10 + count)
                        count += 1
                    }
                }
            }
        }
    }
}