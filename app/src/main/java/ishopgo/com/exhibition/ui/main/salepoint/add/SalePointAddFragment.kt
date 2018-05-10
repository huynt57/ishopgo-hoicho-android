package ishopgo.com.exhibition.ui.main.salepoint.add

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import ishopgo.com.exhibition.ui.main.salepoint.SalePointViewModel
import kotlinx.android.synthetic.main.fragment_sale_point_add.*

class SalePointAddFragment : BaseFragment() {
    private lateinit var viewModel: SalePointViewModel
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()

    companion object {
        fun newInstance(params: Bundle): SalePointAddFragment {
            val fragment = SalePointAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sale_point_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_sale_point_phone.setText(UserDataManager.currentUserPhone)
        edit_sale_point_city.setOnClickListener { getRegion(edit_sale_point_city) }
        edit_sale_point_district.setOnClickListener { getDistrict(edit_sale_point_district) }

        btn_sale_point_add.setOnClickListener {
            if (checkRequireFields(edit_product_name.text.toString(), edit_sale_point_city.text.toString(), edit_sale_point_district.text.toString()))
                viewModel.createSalePoint(edit_product_name.text.toString(), UserDataManager.currentUserPhone, edit_sale_point_city.text.toString(), edit_sale_point_district.text.toString(), edit_sale_point_address.text.toString())
        }
    }

    private fun checkRequireFields(name: String, city: String, district: String): Boolean {

        if (name.trim().isEmpty()) {
            toast("Tên điểm bán không được để trống")
            edit_product_name.error = getString(R.string.error_field_required)
            edit_product_name.requestFocus()
            val inputMethodManager = edit_product_name.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(edit_product_name, 0)
            return false
        }

        if (city.trim().isEmpty()) {
            toast("Bạn chưa chọn thành phố")
            edit_sale_point_city.error = getString(R.string.error_field_required)
            edit_sale_point_city.requestFocus()
            return false
        }

        if (district.trim().isEmpty()) {
            toast("Bạn chưa chọn quận huyện")
            edit_sale_point_district.error = getString(R.string.error_field_required)
            edit_sale_point_district.requestFocus()
            return false
        }

        return true
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thành phố")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getDistrict(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn quận huyện")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterDistrict
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<District> {
                override fun click(position: Int, data: District, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    override
    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SalePointViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.createSusscess.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                activity?.setResult(RESULT_OK)
                toast("Thêm điểm bán thành công")
                activity?.finish()
            }
        })

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.loadDistrict.observe(this, Observer { p ->
            p?.let {
                adapterDistrict.replaceAll(it)
            }
        })

        viewModel.loadRegion()
    }
}