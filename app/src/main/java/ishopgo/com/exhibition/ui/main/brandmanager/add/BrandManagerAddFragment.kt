package ishopgo.com.exhibition.ui.main.brandmanager.add

import android.annotation.SuppressLint
import android.app.Activity
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
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.brandmanager.BrandManagerViewModel
import kotlinx.android.synthetic.main.fragment_brand_add.*

class BrandManagerAddFragment : BaseFragment() {
    private lateinit var viewModel: BrandManagerViewModel
    private var isFeatured: Int = BRAND_NOT_FEATURED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_brand_add, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_image_brands_add.setOnClickListener {
            launchPickPhotoIntent()
        }

        btn_brand_add.setOnClickListener {
            if (isRequiredFieldsValid(image, edit_brand_name.text.toString())) {
                showProgressDialog()
                isFeatured = if (sw_featured.isChecked) BRAND_FEATURED else BRAND_NOT_FEATURED
                viewModel.createBrand(edit_brand_name.text.toString(), image, isFeatured.toString())
            }
        }
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
        viewModel.createSusscess.observe(this, Observer {
            toast("Thêm thành công")
            hideProgressDialog()
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        })
    }

    private fun isRequiredFieldsValid(image: String, name: String): Boolean {
        if (image.trim().isEmpty()) {
            toast("Logo thương hiệu không được để trống")
            return false
        }

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
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(view_image_brands_add)
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    companion object {
        fun newInstance(params: Bundle): BrandManagerAddFragment {
            val fragment = BrandManagerAddFragment()
            fragment.arguments = params

            return fragment
        }

        const val BRAND_FEATURED = 1
        const val BRAND_NOT_FEATURED = 0
    }
}