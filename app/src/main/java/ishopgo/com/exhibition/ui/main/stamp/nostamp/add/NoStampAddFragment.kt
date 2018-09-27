package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BoothAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BrandsAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_no_stamp_add.*

class NoStampAddFragment : BaseFragment() {
    private var stampId = 0L
    private var countStampExist = 0
    private var serialNumber = ""
    private lateinit var viewModel: NoStampViewModel
    private val adapterBrands = BrandsAdapter()
    private val adapterBooth = BoothAdapter()
    private var reloadBrands = false
    private var reloadProvider = false
    private var thuongHieuId: Long = 0L
    private var gianHangId: Long = 0L
    private var productId: Long = 0L
    private var coatings: Int = COATINGS_HIDDEN

    companion object {

        fun newInstance(params: Bundle): NoStampAddFragment {
            val fragment = NoStampAddFragment()
            fragment.arguments = params

            return fragment
        }

        const val COATINGS_SHOW: Int = 1 //Có phủ cào
        const val COATINGS_HIDDEN: Int = 0 //Không phủ cào
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_add, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_no_stamp_thuongHieu.setOnClickListener { getBrands(edit_no_stamp_thuongHieu) }
        edit_no_stamp_gianHang.setOnClickListener { getBooth(edit_no_stamp_gianHang) }
        edit_no_stamp_sanPham.setOnClickListener { viewModel.openSearchProduct(stampId) }
        edit_no_stamp.setOnClickListener { viewModel.getGenerateStamp() }
        sw_coatings.setOnCheckedChangeListener { b, _ ->
            if (b.isChecked) {
                coatings = COATINGS_SHOW
                sw_coatings.text = "Loại tem: Có phủ cào"
            } else {
                coatings = COATINGS_HIDDEN
                sw_coatings.text = "Loại tem: Không phủ cào"
            }
        }
        btn_assign_no_stamp.setOnClickListener {
            if (isRequiredFieldsValid(edit_no_stamp.text.toString(), edit_no_stamp_thuongHieu.text.toString(), edit_no_stamp_gianHang.text.toString(), edit_no_stamp_sanPham.text.toString(), edit_no_stamp_gioiHan.text.toString())) {
                showProgressDialog()
                viewModel.addNoStampDetail(productId, edit_no_stamp.text.toString(), coatings.toString(), edit_no_stamp_gioiHan.text.toString())
            }
        }
    }

    private fun isRequiredFieldsValid(maTem: String, thuongHieu: String, gianHang: String, sanPham: String, gioiHan: String): Boolean {
        if (maTem.trim().isEmpty()) {
            toast("Mã lô không được để trống")
            edit_no_stamp.error = "Trường này còn trống"
            return false
        }

//        if (thuongHieu.trim().isEmpty()) {
//            toast("Thương hiệu không được để trống")
//            edit_no_stamp_thuongHieu.error = "Trường này còn trống"
//            return false
//        }
//
//        if (gianHang.trim().isEmpty()) {
//            toast("Gian hàng không được để trống")
//            edit_no_stamp_gianHang.error = "Trường này còn trống"
//            return false
//        }

        if (sanPham.trim().isEmpty()) {
            toast("Sản phẩm cần gán không được để trống")
            edit_no_stamp_sanPham.error = "Trường này còn trống"
            return false
        }

        if (gioiHan.trim().isEmpty()) {
            toast("Giới hạn cảnh báo không được để trống")
            edit_no_stamp_gioiHan.error = "Trường này còn trống"
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(NoStampViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.generateStamp.observe(this, Observer { p ->
            p?.let {
                edit_no_stamp.setText(it)
            }
        })
//        viewModel.getDataNoStampCreated.observe(this, Observer { p ->
//            p?.let {
//                edit_no_stamp_serial.setText(it.serialNumberPrefix)
//                tv_no_stamp_count_exits.text = "Số lượng tem còn lại: ${it.calculateQuantity ?: 0}"
//                countStampExist = it.calculateQuantity ?: 0
//                serialNumber = it.serialNumberPrefix ?: ""
//            }
//        })

        viewModel.addNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.dataBrands.observe(this, Observer { p ->
            p.let {
                if (reloadBrands)
                    it?.let { it1 -> adapterBrands.replaceAll(it1) }
                else it?.let { it1 -> adapterBrands.addAll(it1) }

            }
        })

        viewModel.dataBooth.observe(this, Observer { p ->
            p?.let {
                if (reloadProvider) adapterBooth.replaceAll(it) else adapterBooth.addAll(it)
            }
        })

        viewModel.resultProduct.observe(this, Observer { p ->
            p?.let {
                productId = it.id
                edit_no_stamp_sanPham.setText(it.name ?: "")
            }
        })

        reloadBrands = true
        reloadProvider = true

        firstLoadBrand()
        firstLoadProvider()
    }

    private fun getBrands(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thương hiệu")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBrands
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreBrands(totalItemsCount)
                }
            })

            adapterBrands.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
                override fun click(position: Int, data: Brand, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        thuongHieuId = data.id
                        view.text = data.name ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getBooth(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBooth
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        gianHangId = data.id
                        view.text = data.boothName ?: ""
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun firstLoadBrand() {
        reloadBrands = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBrand(firstLoad)
    }

    private fun loadMoreBrands(currentCount: Int) {
        reloadBrands = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBrand(loadMore)
    }

    private fun firstLoadProvider() {
        reloadProvider = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBooth(loadMore)
    }
}