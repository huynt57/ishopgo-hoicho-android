package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductDiaryRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.ProductInfo
import kotlinx.android.synthetic.main.fragment_product_detail.view.*

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
class CustomProductDetail : ProductDetailOverwrite() {
    companion object {
        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
        const val DIARY_DELETE_CLICK = 2
    }

    private val adapterDiary = ProductDiaryAdapter()
    private lateinit var viewModelProductDetail: ProductDetailViewModel
    private var productId = -1L
    private fun firstLoadDiary() {
        val firstLoad = ProductDiaryRequest()
        firstLoad.limit = 2
        firstLoad.offset = 0
        firstLoad.productId = productId
        viewModelProductDetail.getProductDiary(firstLoad)
    }

    override fun handleViewCreated(rootView: View, context: Context, fragment: BaseFragment) {
        rootView.apply {
            rv_product_diary.adapter = adapterDiary
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_product_diary.layoutManager = layoutManager
            rv_product_diary.isNestedScrollingEnabled = false
            rv_product_diary.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))

            adapterDiary.listener = object : ClickableAdapter.BaseAdapterAction<DiaryProduct> {
                override fun click(position: Int, data: DiaryProduct, code: Int) {
                    when (code) {
                        DIARY_IMAGE_CLICK -> {
                            if (data.images?.isNotEmpty() == true) {
                                val listImage = ArrayList<String>()
                                for (i in data.images!!.indices)
                                    listImage.add(data.images!![i].image ?: "")
                                val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                                context.startActivity(intent)
                            } else fragment.toast("Không thể mở ảnh này")
                        }

                        DIARY_USER_CLICK -> {
                            val intent = Intent(context, MemberProfileActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                            context.startActivity(intent)
                        }

                        DIARY_DELETE_CLICK -> {
                            context.let {
                                MaterialDialog.Builder(it)
                                        .content("Bạn có muốn xoá nhật ký này không?")
                                        .positiveText("Có")
                                        .onPositive { _, _ ->
                                            viewModelProductDetail.deleteProductDiary(data.id, productId)
                                            fragment.showProgressDialog()
                                        }
                                        .negativeText("Không")
                                        .onNegative { dialog, _ -> dialog.dismiss() }
                                        .show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun handleActivityCreated(viewModel: ProductDetailViewModel, fragment: BaseFragment) {
        viewModelProductDetail = viewModel

        viewModelProductDetail.productDiary.observe(fragment, Observer { p ->
            p.let {
                it?.let { it1 -> adapterDiary.replaceAll(it1) }
            }
        })

        viewModelProductDetail.deleteProductDiary.observe(fragment, Observer { p ->
            p.let {
                fragment.hideProgressDialog()
                fragment.toast("Xoá nhật ký thành công")
                firstLoadDiary()
            }
        })
    }

    override fun handleInOtherFlavor(rootView: View, detail: ProductDetail, fragment: BaseFragment) {
        rootView.apply {
            val listInfo = detail.info ?: mutableListOf()
            val linearLayout = view_content_container as LinearLayout

            productId = detail.id

            if (detail.isNhatkySx == 1) {
                container_diary.visibility = View.VISIBLE

                if (UserDataManager.currentType == "Nhân viên gian hàng") {
                    val listPermission = Const.listPermission
                    if (listPermission.isNotEmpty())
                        for (i in listPermission.indices)
                            if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
                                view_add_diary.visibility = View.VISIBLE
                                break
                            }
                } else if (UserDataManager.currentUserId == detail.booth?.id)
                    view_add_diary.visibility = View.VISIBLE
                else view_add_diary.visibility = View.GONE

                firstLoadDiary()

                view_add_diary.setOnClickListener {
                    val extra = Bundle()
                    extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(detail))
                    Navigation.findNavController(fragment.requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_productDiaryAddFragment, extra)
                }
                view_product_show_more_diary.setOnClickListener {
                    val extra = Bundle()
                    extra.putLong(Const.TransferKey.EXTRA_ID, detail.id)
                    Navigation.findNavController(fragment.requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_productDiaryFragment, extra)
                }
            } else container_diary.visibility = View.GONE

            for (i in listInfo.indices) {
                val productInfo = ProductInfo(rootView.context)
                productInfo.apply {
                    label_products_same_shop.text = listInfo[i].name
                    val adapter = ProductAdapter(0.4f)
                    if (listInfo[i].products?.data?.isNotEmpty() == true) {
                        var count = 0

                        listInfo[i].products?.data?.let { adapter.replaceAll(it) }

                        view_list_products_same_shop.adapter = adapter
                        val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                        layoutManager.isAutoMeasureEnabled = true
                        view_list_products_same_shop.layoutManager = layoutManager
                        view_list_products_same_shop.isNestedScrollingEnabled = false
                        view_list_products_same_shop.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
                        more_products_same_shop.visibility = View.GONE

                        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                            override fun click(position: Int, data: Product, code: Int) {
                                context?.let {
                                    val intent = Intent(context, ProductDetailActivity::class.java)
                                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                                    it.startActivity(intent)
                                }
                            }
                        }

                        linearLayout.addView(productInfo, 11 + count)
                        count += 1
                    }
                }
            }
        }
    }
}