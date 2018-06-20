package ishopgo.com.exhibition.ui.main.product.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_rating_product.*
import com.google.android.gms.common.util.InputMethodUtils.showSoftInput
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class RatingProductFragment : BaseActionBarFragment(), BackpressConsumable {
    override fun contentLayoutRes(): Int {
        return R.layout.fragment_rating_product
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {
        const val TAG = "RatingProductFragment"

        fun newInstance(params: Bundle): RatingProductFragment {
            val fragment = RatingProductFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var ratingViewModel: RatingProductViewModel
    private var ratingNumber: Float = 0.0f
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private var dataProduct: ProductDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        dataProduct = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ratingViewModel = obtainViewModel(RatingProductViewModel::class.java, true)
        ratingViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.postCommentSuccess.observe(this, Observer {
            toast("Tạo thảo luận thành công")
            ratingViewModel.commentSusscess()
            hideProgressDialog()
            edit_comment.setText("")

            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(edit_comment.windowToken, 0)
            activity?.onBackPressed()
        })
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupImageRecycleview()
        edit_comment.requestFocus()
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.showSoftInput(edit_comment, 0)

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingNumber = rating
        }

        tv_add_image.setOnClickListener { launchPickPhotoIntent() }

        btn_comment.setOnClickListener {
            if (checkRequireFields(edit_comment.text.toString())) {
                showProgressDialog()
                dataProduct?.id?.let { it1 -> viewModel.postCommentProduct(it1, edit_comment.text.toString(), 0, postMedias, ratingNumber) }
            }
        }

        if (dataProduct != null) {
            Glide.with(context).load(dataProduct!!.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)
            tv_product.text = dataProduct!!.name
            tv_product_price.text = dataProduct!!.price.asMoney()
            tv_product_code.text = dataProduct!!.code
        }
    }

    private fun checkRequireFields(content: String): Boolean {
        if (ratingNumber == 0.0f) {
            toast("Bạn vui lòng đánh giá sản phẩm")
            return false
        }
        if (content.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
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

    private fun setupToolbar() {
        toolbar.setCustomTitle("Bình luận sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }
}