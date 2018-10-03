package ishopgo.com.exhibition.ui.main.boothmanager.add_booth

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.boothmanager.BoothManagerViewModel
import kotlinx.android.synthetic.main.fragment_booth_manager_add.*

class BoothManagerAddFragment : BaseFragment() {
    private lateinit var viewModel: BoothManagerViewModel
    private val adapterRegion = RegionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_booth_manager_add, container, false)
    }

    companion object {
        fun newInstance(params: Bundle): BoothManagerAddFragment {
            val fragment = BoothManagerAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_booth_region.setOnClickListener {
            getRegion(edit_booth_region)
        }

        view_image_add_booth.setOnClickListener {
            launchPickPhotoIntent()
        }

        btn_product_add.setOnClickListener {
            if (isRequiredFieldsValid(image, edit_booth_name.text.toString(), edit_booth_phone.text.toString(), edit_booth_email.text.toString(), edit_booth_address.text.toString(), edit_booth_region.text.toString(), edit_booth_birthday.text.toString())) {
                showProgressDialog()
                viewModel.createBoothManager(edit_booth_phone.text.toString(), edit_booth_email.text.toString(), edit_booth_name.text.toString(),
                        edit_booth_birthday.text.toString(), edit_booth_address.text.toString(), edit_booth_region.text.toString(), image)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(BoothManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.createSusscess.observe(this, Observer {
            hideProgressDialog()
            toast("Thêm thành công")
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })

        viewModel.loadRegion()
    }

    private fun isRequiredFieldsValid(image: String, phone: String, name: String, email: String, address: String, region: String, birthday: String): Boolean {
        if (image.trim().isEmpty()) {
            toast("Ảnh sản phẩm không được để trống")
            return false
        }

        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            edit_booth_phone.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_phone)
            return false
        }

        if (email.trim().isEmpty()) {
            toast("Email không được để trống")
            edit_booth_email.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_email)
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Tên không được để trống")
            edit_booth_name.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_name)
            return false
        }

        if (birthday.trim().isEmpty()) {
            toast("Ngày sinh không được để trống")
            edit_booth_birthday.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_birthday)
            return false
        }

        if (address.trim().isEmpty()) {
            toast("Địa chỉ không được để trống")
            edit_booth_address.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_address)
            return false
        }

        if (region.trim().isEmpty()) {
            toast("Khu vực không được để trống")
            edit_booth_region.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_booth_region)
            return false
        }
        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn khu vực")
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
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
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
                    .into(view_image_add_booth)
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }
}