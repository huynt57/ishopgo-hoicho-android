package ishopgo.com.exhibition.ui.main.product.detail

import android.content.Context
import android.view.View
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.base.BaseFragment

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
abstract class ProductDetailOverwrite {

    abstract fun handleInOtherFlavor(rootView: View, detail: ProductDetail, fragment: BaseFragment)
    abstract fun handleActivityCreated(rootView: View, viewModel: ProductDetailViewModel, fragment: BaseFragment)
    abstract fun handleViewCreated(rootView: View, context: Context, fragment: BaseFragment)

}