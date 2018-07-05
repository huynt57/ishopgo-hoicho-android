package ishopgo.com.exhibition.ui.main.productmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductManagerRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.productmanager.add.ProductManagerAddActivity
import ishopgo.com.exhibition.ui.main.productmanager.detail.ProductManagerDetailActivity
import ishopgo.com.exhibition.ui.widget.BottomSheetOptionListener
import ishopgo.com.exhibition.ui.widget.OptionsBottomSheet
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ProductManagerFragment : BaseListFragment<List<ProductManager>, ProductManager>() {
    private var name: String = ""
    private var code: String = ""

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_product_manager, container, false)
//    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = ProductManagerRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = name
        firstLoad.code = code
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ProductManagerRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.name = name
        loadMore.code = code
        viewModel.loadData(loadMore)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<ProductManager>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
        hideProgressDialog()
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ProductManager> {
        val adapter = ProductManagerAdapter()
        adapter.addData(ProductManager())
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductManager> {
            override fun click(position: Int, data: ProductManager, code: Int) {
                when (code) {
                    ProductManagerAdapter.CLICK_OPTION -> {
                        val options = mutableListOf<OptionsBottomSheet.Option>()
                        options.add(OptionsBottomSheet.Option("Sửa", R.drawable.ic_edit_default_24dp))
                        if (data.status == 0) // do not display
                            options.add(OptionsBottomSheet.Option("Hiện", R.drawable.ic_visibility_default_24dp))
                        else
                            options.add(OptionsBottomSheet.Option("Ẩn", R.drawable.ic_visibility_off_default_24dp))
                        val bottomSheet = OptionsBottomSheet.Builder()
                                .options(options)
                                .listener(object : BottomSheetOptionListener {
                                    override fun click(code: Int) {
                                        when (code) {
                                            0 -> { // edit
                                                val intent = Intent(context, ProductManagerDetailActivity::class.java)
                                                intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                                                startActivityForResult(intent, Const.RequestCode.PRODUCT_MANAGER_DETAIL)
                                            }
                                            1 -> { // show/hide
                                                displayProduct(data)
                                            }
                                            else -> { // do nothing
                                            }
                                        }
                                    }

                                })
                                .build()

                        bottomSheet.show(childFragmentManager, "Options")

                    }
                    ProductManagerAdapter.CLICK_PUSH_TOP -> {
                        confirmPushTop(data.id)
                    }
                }

            }
        }
        return adapter
    }

    private fun displayProduct(data: ProductManager) {
        if (viewModel is ProductManagerViewModel) {
            val managerViewModel = viewModel as ProductManagerViewModel
            managerViewModel.updateVisibility(data.id, if (data.status == 0) 2 else 0)
        }
    }

    private fun confirmPushTop(productId: Long) {
        MaterialDialog.Builder(requireContext())
                .title("Đẩy sản phẩm này lên top ?".asHtml())
                .content("<font color=\"red\">(*)</font> Chỉ được đẩy top 1 lần/ngày".asHtml())
                .positiveText("OK")
                .onPositive { _, _ ->
                    if (viewModel is ProductManagerViewModel) {
                        (viewModel as ProductManagerViewModel).pushToTop(productId)
                    }
                }
                .negativeText("Hủy")
                .show()
    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductManager>> {
        return obtainViewModel(ProductManagerViewModel::class.java, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is ProductManagerViewModel) {
            val managerViewModel = viewModel as ProductManagerViewModel
            managerViewModel.totalProduct.observe(this, Observer {
                //                tv_number_item.setText(it ?: 0)
            })
            managerViewModel.pushTopSuccess.observe(this, Observer {
                toast("Chức năng đang được hoàn thiện")
            })
            managerViewModel.updateVisibilityOk.observe(this, Observer {
                toast("Thành công")
            })
        }
    }

    fun performSearching() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Tìm kiếm")
                    .customView(R.layout.dialog_search_product, false)
                    .positiveText("Lọc")
                    .onPositive { dialog, _ ->
                        val edt_keyWord = dialog.findViewById(R.id.edt_keyWord) as TextInputEditText
                        val edt_code = dialog.findViewById(R.id.edt_code) as TextInputEditText

                        name = edt_keyWord.text.toString().trim { it <= ' ' }
                        code = edt_code.text.toString().trim { it <= ' ' }

                        dialog.dismiss()

                        showProgressDialog()
                        firstLoad()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edt_keyWord = dialog.findViewById(R.id.edt_keyWord) as TextInputEditText
            val edt_code = dialog.findViewById(R.id.edt_code) as TextInputEditText
            edt_keyWord.setText(name)
            edt_code.setText(code)

            dialog.show()
        }
    }

    fun openAddProductManager() {
        val intent = Intent(context, ProductManagerAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.PRODUCT_MANAGER_ADD)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
    }

    companion object {
        const val TAG = "ProductManagerFragment"
        fun newInstance(params: Bundle): ProductManagerFragment {
            val fragment = ProductManagerFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.PRODUCT_MANAGER_ADD && resultCode == Activity.RESULT_OK) {
            showProgressDialog()
            firstLoad()
        }

        if (requestCode == Const.RequestCode.PRODUCT_MANAGER_DETAIL && resultCode == Activity.RESULT_OK) {
            showProgressDialog()
            firstLoad()
        }
    }
}