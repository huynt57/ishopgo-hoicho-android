package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.product.detail.DiaryProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_product_diary_add.*

class ProductDiaryAddFragment : BaseActionBarFragment() {
    private var data = ProductDetail()
    private lateinit var viewModel: ProductDetailViewModel
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private lateinit var viewModelDiary: DiaryProductViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_diary_add
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        tv_add_image.setOnClickListener { launchPickPhotoIntent() }

        btn_add_diary.setOnClickListener {
            if (isRequiredFieldsValid(edit_title.text.toString(), edit_content.text.toString())) {
                showProgressDialog()
                viewModel.createProductDiary(data.id, edit_title.text.toString(), edit_content.text.toString(), postMedias)
            }
        }

        Glide.with(context).load(data.image)
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(img_product)
        tv_product.text = data.name
        tv_product_price.text = data.price.asMoney()
        tv_product_code.text = data.code

        setupImageRecycleview()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelDiary = obtainViewModel(DiaryProductViewModel::class.java, true)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.createProductDiary.observe(this, Observer { p ->
            p?.let {
                toast("Tạo nhật ký sản xuất thành công")
                viewModelDiary.createDiarySusscess()
                hideProgressDialog()
                hideKeyboard()
                activity?.onBackPressed()
            }
        })
    }

    private fun isRequiredFieldsValid(title: String, content: String): Boolean {
        if (title.trim().isEmpty()) {
            toast("Tiêu đề không được để trống")
            return false
        }
        if (content.trim().isEmpty()) {
            toast("Nội dung không được để trống")
            return false
        }
        return true
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_image.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            rv_image.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_image.adapter = adapterImages
            adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMedias.remove(data)
                    if (postMedias.isEmpty()) rv_image.visibility = View.GONE
                    adapterImages.replaceAll(postMedias)
                }
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages.replaceAll(postMedias)
        }
    }

    private fun setupToolbars() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.setCustomTitle("Thêm nhật ký sản xuất")
    }
}