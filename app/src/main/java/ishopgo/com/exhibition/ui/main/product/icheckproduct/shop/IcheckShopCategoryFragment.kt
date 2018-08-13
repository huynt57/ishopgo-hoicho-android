package ishopgo.com.exhibition.ui.main.product.icheckproduct.shop

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_icheck_category_product.*

class IcheckShopCategoryFragment : BaseFragment() {

    companion object {
        const val TAG = "IcheckShopCategoryFragment"
        fun newInstance(params: Bundle): IcheckShopCategoryFragment {
            val fragment = IcheckShopCategoryFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: IcheckProductViewModel
    private var shopId = 0L
    private val adapter = IcheckProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_icheck_category_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            view_recyclerview.adapter = adapter
            view_recyclerview.setHasFixedSize(true)
            val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            view_recyclerview.layoutManager = layoutManager
            view_recyclerview.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(IcheckProductViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.dataShopCategory.observe(this, Observer { p ->
            p?.let {
                val adapter = SpIcheckCategoryAdapter(it)
                sp_category.adapter = adapter
                sp_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (it.isNotEmpty()) {
                            firstLoad(it[position].id)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        })

        viewModel.dataShopCategoryProduct.observe(this, Observer { p ->
            p.let {
                adapter.replaceAll(it ?: mutableListOf())
            }
        })

        val request = String.format("https://core.icheck.com.vn/vendors/%s/categories", shopId)
        viewModel.loadIcheckShopCategory(request)
    }

    private fun firstLoad(categoryId: Long) {
        view_recyclerview.scheduleLayoutAnimation()
        val request = String.format("https://core.icheck.com.vn/vendors/%s/categories/%s/products", shopId, categoryId)
        viewModel.loadIcheckShopCategoryProduct(request)
    }
}
