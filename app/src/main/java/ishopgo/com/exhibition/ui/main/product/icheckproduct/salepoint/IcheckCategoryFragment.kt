package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckCategory
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import kotlinx.android.synthetic.main.content_icheck_category.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckCategoryFragment : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    private lateinit var viewModel: IcheckProductViewModel
    private var categoryId = 0L
    private var categoryName = ""
    private val adapterCategory_1 = IcheckCategoryAdapter()
    private val adapterCategory_2 = IcheckCategoryAdapter()
    private val adapterCategory_3 = IcheckCategoryAdapter()
    private val adapterCategory_4 = IcheckCategoryAdapter()

    companion object {
        const val TAG = "IcheckCategoryFragment"
        fun newInstance(params: Bundle): IcheckCategoryFragment {
            val fragment = IcheckCategoryFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_icheck_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()
        setupRecyclerView()

        adapterCategory_1.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
            override fun click(position: Int, data: IcheckCategory, code: Int) {
                context?.let {
                    if (data.childrens ?: 0 > 0) {
                        val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                        viewModel.loadIcheckCategory_2(request)
                    } else {
                        btn_add_category.isClickable = true
                        categoryId = data.id
                        categoryName = data.name ?:""
                    }
                }
            }
        }

        adapterCategory_2.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
            override fun click(position: Int, data: IcheckCategory, code: Int) {
                context?.let {
                    if (data.childrens ?: 0 > 0) {
                        val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                        viewModel.loadIcheckCategory_3(request)
                    } else {
                        categoryId = data.id
                        categoryName = data.name ?:""
                        btn_add_category.isClickable = true
                    }
                }
            }
        }

        adapterCategory_3.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
            override fun click(position: Int, data: IcheckCategory, code: Int) {
                context?.let {
                    if (data.childrens ?: 0 > 0) {
                        val request = String.format("https://api-affiliate.icheck.com.vn:6086/categories?parent_id=%s", data.id)
                        viewModel.loadIcheckCategory_4(request)
                    } else {
                        categoryId = data.id
                        categoryName = data.name ?:""
                        btn_add_category.isClickable = true
                    }
                }
            }
        }

        adapterCategory_4.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCategory> {
            override fun click(position: Int, data: IcheckCategory, code: Int) {
                context?.let {
                    categoryId = data.id
                    categoryName = data.name ?:""
                    btn_add_category.isClickable = true
                }
            }
        }

        btn_add_category.setOnClickListener {
            if (categoryId > 0L){
                viewModel.resultCateogory(categoryId, categoryName)
                activity?.onBackPressed()
            }
            else toast("Bạn vui lòng chọn danh mục cuối cùng")
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chọn danh mục")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
    }

    private fun setupRecyclerView() {
        context?.let {
            rv_categories_1.setHasFixedSize(true)
            rv_categories_1.layoutManager = LinearLayoutManager(it, LinearLayout.VERTICAL, false)
            rv_categories_1.adapter = adapterCategory_1

            rv_categories_2.setHasFixedSize(true)
            rv_categories_2.layoutManager = LinearLayoutManager(it, LinearLayout.VERTICAL, false)
            rv_categories_2.adapter = adapterCategory_2

            rv_categories_3.setHasFixedSize(true)
            rv_categories_3.layoutManager = LinearLayoutManager(it, LinearLayout.VERTICAL, false)
            rv_categories_3.adapter = adapterCategory_3

            rv_categories_4.setHasFixedSize(true)
            rv_categories_4.layoutManager = LinearLayoutManager(it, LinearLayout.VERTICAL, false)
            rv_categories_4.adapter = adapterCategory_4
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { baseErrorSignal ->
            baseErrorSignal?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.dataCategory.observe(this, Observer {
            rv_categories_1.visibility = View.VISIBLE
            rv_categories_2.visibility = View.GONE
            rv_categories_3.visibility = View.GONE
            rv_categories_4.visibility = View.GONE
            it?.let { it1 -> adapterCategory_1.replaceAll(it1) }
        })

        viewModel.dataCategory_2.observe(this, Observer {
            rv_categories_1.visibility = View.GONE
            rv_categories_2.visibility = View.VISIBLE
            rv_categories_3.visibility = View.GONE
            rv_categories_4.visibility = View.GONE
            it?.let { it1 -> adapterCategory_2.replaceAll(it1) }
        })

        viewModel.dataCategory_3.observe(this, Observer {
            rv_categories_1.visibility = View.GONE
            rv_categories_2.visibility = View.GONE
            rv_categories_3.visibility = View.VISIBLE
            rv_categories_4.visibility = View.GONE
            it?.let { it1 -> adapterCategory_3.replaceAll(it1) }
        })

        viewModel.dataCategory_4.observe(this, Observer {
            rv_categories_1.visibility = View.GONE
            rv_categories_2.visibility = View.GONE
            rv_categories_3.visibility = View.GONE
            rv_categories_4.visibility = View.VISIBLE
            it?.let { it1 -> adapterCategory_4.replaceAll(it1) }
        })

        viewModel.loadIcheckCategory("https://api-affiliate.icheck.com.vn:6086/categories")
    }
}