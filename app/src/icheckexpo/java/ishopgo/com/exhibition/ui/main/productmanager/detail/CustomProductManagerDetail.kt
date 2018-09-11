package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.content.Context
import android.view.View
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel

class CustomProductManagerDetail : ProductManagerDetailOverwrite() {
    override fun handleInOtherFlavor(rootView: View, detail: ProductDetail, fragment: BaseFragment) {
    }

    override fun handleViewCreated(rootView: View, context: Context, listProductRelated: ArrayList<Product>, listVatTu: ArrayList<Product>, listGiaiPhap: ArrayList<Product>) {
    }

    override fun handleStartEdit(rootView: View) {
    }

    override fun handleEndEdit(rootView: View) {
    }

    override fun handleOnCreate(productId: Long) {
    }

    override fun handleActivityCreated(viewModel: ProductManagerViewModel, fragment: BaseFragment) {
    }
}