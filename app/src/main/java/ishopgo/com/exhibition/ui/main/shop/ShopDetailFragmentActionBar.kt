package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryFragment
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    companion object {
        fun newInstance(params: Bundle): ShopDetailFragmentActionBar {
            val fragment = ShopDetailFragmentActionBar()
            fragment.arguments = params
            return fragment
        }
    }

    private lateinit var shareViewModel: ShopDetailShareViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, DetailFragment.newInstance(arguments ?: Bundle()))
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shareViewModel = obtainViewModel(ShopDetailShareViewModel::class.java, true)
        shareViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        shareViewModel.showCategoriedProducts.observe(this, Observer { s ->
            s?.let {
                if (it is Category) {
                    val params = Bundle()
                    params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.getDefaultGson().toJson(it))
                    childFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                            .add(R.id.frame_main_content, ProductsByCategoryFragment.newInstance(params))
                            .addToBackStack(ProductsByCategoryFragment.TAG)
                            .commit()
                }
            }
        })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}