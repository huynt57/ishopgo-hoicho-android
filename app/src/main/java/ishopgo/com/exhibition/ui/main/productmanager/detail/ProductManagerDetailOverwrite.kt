package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.content.Context
import android.view.View
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel

abstract class ProductManagerDetailOverwrite {
    abstract fun handleInOtherFlavor(rootView: View, detail: ProductDetail, fragment: BaseFragment)
    abstract fun handleViewCreated(rootView: View, context: Context, listProductRelated: ArrayList<Product>, listVatTu: ArrayList<Product>, listGiaiPhap: ArrayList<Product>)
    abstract fun handleStartEdit(rootView: View)
    abstract fun handleEndEdit(rootView: View)
    abstract fun handleOnCreate(productId: Long)
    abstract fun handleActivityCreated(viewModel: ProductManagerViewModel, fragment: BaseFragment)
}