package ishopgo.com.exhibition.ui.main.brandmanager.update

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.brandmanager.BrandManagerViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_brand_update.*

class BrandManagerUpdateFragment : BaseFragment() {
    private lateinit var viewModel: BrandManagerViewModel
    private var data: Brand? = null
    private var isFeatured: Int = BRAND_NOT_FEATURED


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_brand_update, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        data = Toolbox.getDefaultGson().fromJson(json, Brand::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_image_brands_update.setOnClickListener {
            launchPickPhotoIntent()
        }

        edit_brand_name.setText(data?.provideName())

        Glide.with(context)
                .load(data?.provideLogo())
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(view_image_brands_update)

        btn_brand_update.setOnClickListener {
            if (checkRequireFields(edit_brand_name.text.toString())) {
                showProgressDialog()
                isFeatured = data?.isFeatured ?: BRAND_NOT_FEATURED
                viewModel.updateBrand(data?.id
                        ?: -1L, edit_brand_name.text.toString(), image, isFeatured.toString())
            }
        }
    }

    fun deleteBrand() {
        viewModel.deleteMember(data?.id ?: -1L)
        showProgressDialog()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(BrandManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.updateSusscess.observe(this, Observer {
            toast("Cập nhật thành công")
            hideProgressDialog()
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })

        viewModel.deleteSusscess.observe(this, Observer {
            toast("Xoá thành công")
            hideProgressDialog()
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })
    }

    private fun checkRequireFields( name: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tên thương hiệu không được để trống")
            edit_brand_name.error = getString(R.string.error_field_required)
            edit_brand_name.requestFocus()
            val inputMethodManager = edit_brand_name.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(edit_brand_name, 0)
            return false
        }

        return true
    }

    private var image = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(view_image_brands_update)
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    companion object {
        const val TAG = "BrandManagerUpdateFragment"
        fun newInstance(params: Bundle): BrandManagerUpdateFragment {
            val fragment = BrandManagerUpdateFragment()
            fragment.arguments = params

            return fragment
        }

        const val BRAND_NOT_FEATURED = 0
    }
}