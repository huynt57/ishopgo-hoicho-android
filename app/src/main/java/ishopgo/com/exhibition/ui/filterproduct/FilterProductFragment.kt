package ishopgo.com.exhibition.ui.filterproduct

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_filter_product.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class FilterProductFragment : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    private val handleOverwrite: FilterProductOverwrite = CustomFilterProduct()

    private lateinit var viewModel: FilterProductViewModel
    private val listTypeFilter = mutableListOf<Int>()
    private var type_filter = 0
    private var sort_by: String? = null
    private var sort_type: String? = null
    private var data: FilterProduct? = null

    companion object {
        const val TAG = "FilterProductFragment"

        fun newInstance(params: Bundle): FilterProductFragment {
            val fragment = FilterProductFragment()
            fragment.arguments = params

            return fragment
        }

        const val TYPE_FILTER_PROMOTION = 2
        const val TYPE_FILTER_CARE = 1
        const val TYPE_FILTER_SUPPLY = 3
        const val TYPE_FILTER_WHOLESALE = 4
        const val TYPE_FILTER_NKSX = 5
        const val SORT_BY_NAME = "name"
        const val SORT_BY_FOLLOW = "follow"
        const val SORT_BY_RATE = "rate"
        const val SORT_BY_PRICE = "price"
        const val SORT_TYPE_ASC = "asc"
        const val SORT_TYPE_DESC = "desc"
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_filter_product
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, FilterProduct::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()

        handleOverwrite.handleInOtherFlavor(view)

        if (data != null) {
            val data_sort_type = data!!.sort_type
            val data_sort_by = data!!.sort_by
            val data_filter = data!!.filter

            if (data_filter?.isNotEmpty() == true) {
                listTypeFilter.addAll(data_filter)
                for (i in data_filter.indices) {
                    if (data_filter[i] == TYPE_FILTER_PROMOTION) {
                        tv_promotion.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                    if (data_filter[i] == TYPE_FILTER_CARE) {
                        tv_care.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                    if (data_filter[i] == TYPE_FILTER_SUPPLY) {
                        tv_supply.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                    if (data_filter[i] == TYPE_FILTER_WHOLESALE) {
                        tv_wholesale.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                    if (data_filter[i] == TYPE_FILTER_NKSX) {
                        tv_nksx.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                }
            }

            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_PRICE) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_PRICE
                tv_price_high_to_low.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            if (data_sort_type == SORT_TYPE_ASC && data_sort_by == SORT_BY_PRICE) {
                sort_type = SORT_TYPE_ASC
                sort_by = SORT_BY_PRICE
                tv_price_low_to_high.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            if (data_sort_type == SORT_TYPE_ASC && data_sort_by == SORT_BY_NAME) {
                sort_type = SORT_TYPE_ASC
                sort_by = SORT_BY_NAME
                tv_a_z.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_NAME) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_NAME
                tv_z_a.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_FOLLOW) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_FOLLOW
                tv_care_high_to_low.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_RATE) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_RATE
                tv_evaluation_high_to_low.setTextColor(resources.getColor(R.color.colorPrimary))
            }

        }

        tv_price_high_to_low.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_PRICE
            selectSortType(tv_price_high_to_low)
        }
        tv_price_low_to_high.setOnClickListener {
            sort_type = SORT_TYPE_ASC
            sort_by = SORT_BY_PRICE
            selectSortType(tv_price_low_to_high)
        }
        tv_a_z.setOnClickListener {
            sort_type = SORT_TYPE_ASC
            sort_by = SORT_BY_NAME
            selectSortType(tv_a_z)
        }
        tv_z_a.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_NAME
            selectSortType(tv_z_a)
        }
        tv_care_high_to_low.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_FOLLOW
            selectSortType(tv_care_high_to_low)
        }

        tv_evaluation_high_to_low.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_RATE
            selectSortType(tv_evaluation_high_to_low)
        }

        tv_promotion.setOnClickListener {
            type_filter = TYPE_FILTER_PROMOTION
            selectFilterType(tv_promotion)
        }
        tv_care.setOnClickListener {
            type_filter = TYPE_FILTER_CARE
            selectFilterType(tv_care)
        }
        tv_supply.setOnClickListener {
            type_filter = TYPE_FILTER_SUPPLY
            selectFilterType(tv_supply)
        }
        tv_wholesale.setOnClickListener {
            type_filter = TYPE_FILTER_WHOLESALE
            selectFilterType(tv_wholesale)
        }
        tv_nksx.setOnClickListener {
            type_filter = TYPE_FILTER_NKSX
            selectFilterType(tv_nksx)
        }

        btn_filter.setOnClickListener {
            val data = FilterProduct()
            data.sort_by = sort_by
            data.sort_type = sort_type
            data.filter = listTypeFilter

            viewModel.getDataFilter(data)

            activity?.onBackPressed()
        }

        constraintLayout.setOnClickListener(null)
    }

    private fun selectSortType(view: VectorSupportTextView) {
        tv_price_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_price_low_to_high.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_a_z.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_z_a.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_care_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_evaluation_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))

        view.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun selectFilterType(view: VectorSupportTextView) {
        if (listTypeFilter.isEmpty()) {
            listTypeFilter.add(type_filter)
            view.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            var forEnd = false
            for (i in listTypeFilter.indices)
                if (listTypeFilter[i] == type_filter) {
                    listTypeFilter.remove(type_filter)
                    view.setTextColor(resources.getColor(R.color.md_grey_800))
                    break
                } else if (i == listTypeFilter.size - 1) forEnd = true

            if (forEnd) {
                listTypeFilter.add(type_filter)
                view.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(FilterProductViewModel::class.java, true)
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Lọc sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }
}