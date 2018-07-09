package ishopgo.com.exhibition.ui.FilterProduct

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_fillter_product.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class FilterProductFragment : BaseActionBarFragment() {
    private lateinit var viewModel: FilterProductViewModel
    private val listTypeFilter = mutableListOf<Int>()
    private var type_filter = 0
    private var sort_by = SORT_BY_NAME
    private var sort_type = SORT_TYPE_ASC

    companion object {
        const val TAG = "FilterProductFragment"

        fun newInstance(params: Bundle): FilterProductFragment {
            val fragment = FilterProductFragment()
            fragment.arguments = params

            return fragment
        }

        const val TYPE_FILTER_PROMOTION = 1
        const val TYPE_FILTER_CARE = 2
        const val SORT_BY_NAME = "name"
        const val SORT_BY_FOLLOW = "follow"
        const val SORT_BY_RATE = "rate"
        const val SORT_BY_PRICE = "price"
        const val SORT_TYPE_ASC = "asc"
        const val SORT_TYPE_DESC = "desc"
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_fillter_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbars()

        tv_a_z.setTextColor(resources.getColor(R.color.colorPrimary))

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
        tv_care_low_to_high.setOnClickListener {
            sort_type = SORT_TYPE_ASC
            sort_by = SORT_BY_FOLLOW
            selectSortType(tv_care_low_to_high)
        }
        tv_evaluation_high_to_low.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_RATE
            selectSortType(tv_evaluation_high_to_low)
        }
        tv_evaluation_low_to_high.setOnClickListener {
            sort_type = SORT_TYPE_ASC
            sort_by = SORT_BY_RATE
            selectSortType(tv_evaluation_low_to_high)
        }

        tv_promotion.setOnClickListener {
            type_filter = TYPE_FILTER_PROMOTION
            selectFilterType(tv_promotion)
        }
        tv_care.setOnClickListener {
            type_filter = TYPE_FILTER_CARE
            selectFilterType(tv_care)
        }

        btn_filter.setOnClickListener {
            val data = FilterProduct()
            data.sort_by = sort_by
            data.sort_type = sort_type
            data.filter = listTypeFilter

            viewModel.getDataFilter(data)

            activity?.onBackPressed()
        }
    }

    private fun selectSortType(view: VectorSupportTextView) {
        tv_price_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_price_low_to_high.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_a_z.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_z_a.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_care_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_care_low_to_high.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_evaluation_high_to_low.setTextColor(resources.getColor(R.color.md_grey_800))
        tv_evaluation_low_to_high.setTextColor(resources.getColor(R.color.md_grey_800))

        view.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun selectFilterType(view: VectorSupportTextView) {
        if (listTypeFilter.isEmpty()) {
            listTypeFilter.add(type_filter)
            view.setTextColor(resources.getColor(R.color.colorPrimary))

        } else {
            for (i in listTypeFilter.indices)
                if (listTypeFilter[i] == type_filter) {
                    listTypeFilter.remove(type_filter)
                    view.setTextColor(resources.getColor(R.color.md_grey_800))
                    break
                } else {
                    listTypeFilter.add(type_filter)
                    view.setTextColor(resources.getColor(R.color.colorPrimary))
                    break
                }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(FilterProductViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Lọc sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }
}