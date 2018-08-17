package ishopgo.com.exhibition.ui.main.stamp.nostamp.assign

import android.annotation.SuppressLint
import android.app.Activity
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
import kotlinx.android.synthetic.main.content_no_stamp_assign.*

class NoStampAssignFragment : BaseFragment() {
    private var stampId = 0L
    private var countStampExist = 0
    private lateinit var viewModel: NoStampViewModel
    private val adapterBrands = BrandsAdapter()
    private val adapterBooth = BoothAdapter()
    private var reloadBrands = false
    private var reloadProvider = false
    private var thuongHieuId: Long = 0L
    private var gianHangId: Long = 0L
    private var productId: Long = 0L

    companion object {

        fun newInstance(params: Bundle): NoStampAssignFragment {
            val fragment = NoStampAssignFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stampId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
        countStampExist = arguments?.getInt(Const.TransferKey.EXTRA_STAMP_COUNT, 0) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_assign, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_no_stamp_thuongHieu.setOnClickListener { getBrands(edit_no_stamp_thuongHieu) }
        edit_no_stamp_gianHang.setOnClickListener { getBooth(edit_no_stamp_gianHang) }
        edit_no_stamp_sanPham.setOnClickListener { viewModel.openSearchProduct(stampId) }
        tv_no_stamp_count_exits.text = "Số lượng tem còn lại: $countStampExist"

        btn_assign_no_stamp.setOnClickListener {
            if (isRequiredFieldsValid(edit_no_stamp_count.text.toString().toLong(), edit_no_stamp_sanPham.text.toString())) {
                viewModel.saveStampAssign(stampId, productId, edit_no_stamp_gioiHan.text.toString(), edit_no_stamp_noiDung.text.toString(), edit_no_stamp_count.text.toString())
            }
        }
    }

    private fun isRequiredFieldsValid(countStamp: Long, name: String): Boolean {
        if (countStamp > countStampExist) {
            toast("Số lượng tem không được lớn hơn số lượng tem còn lại")
            edit_no_stamp_count.error = "Vui lòng nhập lại"
            edit_no_stamp_count.requestFocus()
            val inputMethodManager = edit_no_stamp_count.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Sản phẩm không được để trống")
            edit_no_stamp_sanPham.error = "Trường này còn trống"
            edit_no_stamp_count.requestFocus()
            val inputMethodManager = edit_no_stamp_sanPham.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
            return false
        }

        return true
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
}