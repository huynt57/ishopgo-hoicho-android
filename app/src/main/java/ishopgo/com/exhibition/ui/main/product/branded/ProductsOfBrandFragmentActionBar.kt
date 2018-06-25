package ishopgo.com.exhibition.ui.main.product.branded

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductsOfBrandFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): ProductsOfBrandFragmentActionBar {
            val fragment = ProductsOfBrandFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = activity?.intent?.getStringExtra(Const.TransferKey.EXTRA_JSON)
        brand = Toolbox.gson.fromJson(json, Brand::class.java)

        setupToolbars()

        val extra = Bundle()
        extra.putLong(Const.TransferKey.EXTRA_ID, brand.id)
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ProductsOfBrandFragment.newInstance(extra))
                .commit()
    }

    private lateinit var brand: Brand

    private fun setupToolbars() {
        toolbar.setCustomTitle(brand.name ?: "Sản phẩm cùng thương hiệu")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(brand))
            Navigation.findNavController(it).navigate(R.id.action_productsOfBrandFragmentActionBar_to_searchProductsOfBrandFragment, extra)
        }
    }

}