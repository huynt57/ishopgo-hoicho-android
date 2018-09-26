package ishopgo.com.exhibition.ui.filterproduct

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.content_filter_product.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import android.support.v7.app.AppCompatDelegate


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
        const val TYPE_FILTER_RETAIL = 6
        const val TYPE_FILTER_DIARY = 5
        const val TYPE_FILTER_QRCODE = 7
        const val TYPE_FILTER_KNCU = 3
        const val TYPE_FILTER_WHOLESALE = 4

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

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val cb_QrCode = resources.getDrawable(R.drawable.ic_filter_qrcode_highlight_24dp)
        cb_qrCode.setCompoundDrawablesWithIntrinsicBounds(cb_QrCode, null, null, null)

        val cb_Promotion = resources.getDrawable(R.drawable.ic_filter_sale_highlight_24dp)
        cb_promotion.setCompoundDrawablesWithIntrinsicBounds(cb_Promotion, null, null, null)

        val cb_Care = resources.getDrawable(R.drawable.ic_filter_care_highlight_24dp)
        cb_care.setCompoundDrawablesWithIntrinsicBounds(cb_Care, null, null, null)

        val cb_Retail = resources.getDrawable(R.drawable.ic_filter_price_highlight_24dp)
        cb_retail.setCompoundDrawablesWithIntrinsicBounds(cb_Retail, null, null, null)

        val cb_Wholesale = resources.getDrawable(R.drawable.ic_filter_price_highlight_24dp)
        cb_wholesale.setCompoundDrawablesWithIntrinsicBounds(cb_Wholesale, null, null, null)

        val cb_nksx = resources.getDrawable(R.drawable.ic_filter_cart_highlight_24dp)
        cb_NKSX.setCompoundDrawablesWithIntrinsicBounds(cb_nksx, null, null, null)

        val cb_kncu = resources.getDrawable(R.drawable.ic_filter_refresh_highlight_24dp)
        cb_KNCU.setCompoundDrawablesWithIntrinsicBounds(cb_kncu, null, null, null)

        val rb_Price_high = resources.getDrawable(R.drawable.ic_sort_price_down_highlight_24dp)
        rb_price_high_to_low.setCompoundDrawablesWithIntrinsicBounds(rb_Price_high, null, null, null)

        val rb_Price_low = resources.getDrawable(R.drawable.ic_sort_price_up_highlight_24dp)
        rb_price_low_to_high.setCompoundDrawablesWithIntrinsicBounds(rb_Price_low, null, null, null)

        val rb_Rating = resources.getDrawable(R.drawable.ic_sort_rating_highlight_24dp)
        rb_rating.setCompoundDrawablesWithIntrinsicBounds(rb_Rating, null, null, null)

        val rb_Care = resources.getDrawable(R.drawable.ic_sort_care_highlight_24dp)
        rb_care.setCompoundDrawablesWithIntrinsicBounds(rb_Care, null, null, null)


        handleOverwrite.handleInOtherFlavor(view)

        if (data != null) {
            val data_sort_type = data!!.sort_type
            val data_sort_by = data!!.sort_by
            val data_filter = data!!.filter

            if (data_filter?.isNotEmpty() == true) {
                listTypeFilter.addAll(data_filter)
                for (i in data_filter.indices) {
                    if (data_filter[i] == TYPE_FILTER_PROMOTION) {
                        cb_promotion.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_CARE) {
                        cb_care.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_RETAIL) {
                        cb_retail.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_WHOLESALE) {
                        cb_wholesale.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_DIARY) {
                        cb_NKSX.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_QRCODE) {
                        cb_qrCode.isChecked = true
                    }
                    if (data_filter[i] == TYPE_FILTER_KNCU) {
                        cb_KNCU.isChecked = true
                    }
                }
            }

            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_PRICE) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_PRICE
                rb_price_high_to_low.isChecked = true
            }

            if (data_sort_type == SORT_TYPE_ASC && data_sort_by == SORT_BY_PRICE) {
                sort_type = SORT_TYPE_ASC
                sort_by = SORT_BY_PRICE
                rb_price_low_to_high.isChecked = true
            }

            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_RATE) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_RATE
                rb_rating.isChecked = true
            }

            if (data_sort_type == SORT_TYPE_DESC && data_sort_by == SORT_BY_FOLLOW) {
                sort_type = SORT_TYPE_DESC
                sort_by = SORT_BY_FOLLOW
                rb_care.isChecked = true
            }
        }

        rb_price_high_to_low.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_PRICE
            selectSortType(rb_price_high_to_low)
        }
        rb_price_low_to_high.setOnClickListener {
            sort_type = SORT_TYPE_ASC
            sort_by = SORT_BY_PRICE
            selectSortType(rb_price_low_to_high)
        }
        rb_rating.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_RATE
            selectSortType(rb_rating)
        }

        rb_care.setOnClickListener {
            sort_type = SORT_TYPE_DESC
            sort_by = SORT_BY_FOLLOW
            selectSortType(rb_care)
        }

        cb_promotion.setOnClickListener {
            type_filter = TYPE_FILTER_PROMOTION
            selectFilterType(cb_promotion)
        }
        cb_care.setOnClickListener {
            type_filter = TYPE_FILTER_CARE
            selectFilterType(cb_care)
        }
        cb_wholesale.setOnClickListener {
            type_filter = TYPE_FILTER_WHOLESALE
            selectFilterType(cb_wholesale)
        }
        cb_retail.setOnClickListener {
            type_filter = TYPE_FILTER_RETAIL
            selectFilterType(cb_retail)
        }
        cb_KNCU.setOnClickListener {
            type_filter = TYPE_FILTER_KNCU
            selectFilterType(cb_KNCU)
        }
        cb_NKSX.setOnClickListener {
            type_filter = TYPE_FILTER_DIARY
            selectFilterType(cb_NKSX)
        }
        cb_qrCode.setOnClickListener {
            type_filter = TYPE_FILTER_QRCODE
            selectFilterType(cb_qrCode)
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

    private fun selectSortType(view: RadioButton) {
        view.isChecked = true
    }

    private fun selectFilterType(view: CheckBox) {
        if (listTypeFilter.isEmpty()) {
            listTypeFilter.add(type_filter)
            view.isChecked = true
        } else {
            var forEnd = false
            for (i in listTypeFilter.indices)
                if (listTypeFilter[i] == type_filter) {
                    listTypeFilter.remove(type_filter)
                    view.isChecked = false
                    break
                } else if (i == listTypeFilter.size - 1) forEnd = true

            if (forEnd) {
                listTypeFilter.add(type_filter)
                view.isChecked = true
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