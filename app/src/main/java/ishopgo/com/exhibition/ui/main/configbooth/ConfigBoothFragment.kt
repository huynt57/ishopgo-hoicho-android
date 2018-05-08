package ishopgo.com.exhibition.ui.main.configbooth

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
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_setting_booth.*


/**
 * Created by hoangnh on 5/5/2018.
 */
class ConfigBoothFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_booth, container, false)
    }

    private var isEditMode = false

    private lateinit var viewModel: ConfigBoothViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_create_booth.setOnClickListener {
            if (!isEditMode) {
                startEditing()
                isEditMode = true
            } else {
                if (checkRequireFields(tv_setting_booth_name.text.toString())) {
                    showProgressDialog()
                    viewModel.editConfigBooth(tv_setting_booth_name.text.toString(), tv_setting_booth_hotline.text.toString(),
                            tv_setting_booth_infor.text.toString(), tv_setting_booth_introduction.text.toString(), tv_setting_booth_address.text.toString(), image)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ConfigBoothViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.editSusscess.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                stopEditing()
                isEditMode = false
                toast("Cập nhật thành thành công")
            }
        })

        viewModel.getConfigBooth.observe(this, Observer {
            it?.let {
                tv_setting_booth_name.setText(it.provideName())
                tv_setting_booth_hotline.setText(it.provideHotline())
                tv_setting_booth_introduction.setText(it.provideIntroduction())
                tv_setting_booth_infor.setText(it.provideInfo())
                tv_setting_booth_address.setText(it.provideAddress())

                Glide.with(context)
                        .load(it.provideBanner())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(img_setting_booth)
            }
        })

        viewModel.getConfigBooth()
    }

    private var image = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context, data.data, (2 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_setting_booth)
        }
    }

    private fun checkRequireFields(name: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tên gian hàng không được để trống")
            tv_setting_booth_name.error = "Trường này còn trống"
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun stopEditing() {
        img_setting_booth.setOnClickListener(null)
        tv_setting_booth_name.isFocusable = false
        tv_setting_booth_name.isFocusableInTouchMode = false
        tv_setting_booth_hotline.isFocusable = false
        tv_setting_booth_hotline.isFocusableInTouchMode = false
        tv_setting_booth_introduction.isFocusable = false
        tv_setting_booth_introduction.isFocusableInTouchMode = false
        tv_setting_booth_infor.isFocusable = false
        tv_setting_booth_infor.isFocusableInTouchMode = false
        tv_setting_booth_address.isFocusable = false
        tv_setting_booth_address.isFocusableInTouchMode = false

        view_scrollview.smoothScrollTo(0, 0)
        btn_create_booth.text = "Cập nhật"
    }

    @SuppressLint("SetTextI18n")
    private fun startEditing() {
        img_setting_booth.setOnClickListener {
            launchPickPhotoIntent()
        }
        tv_setting_booth_name.isFocusable = true
        tv_setting_booth_name.isFocusableInTouchMode = true
        tv_setting_booth_hotline.isFocusable = true
        tv_setting_booth_hotline.isFocusableInTouchMode = true
        tv_setting_booth_introduction.isFocusable = true
        tv_setting_booth_introduction.isFocusableInTouchMode = true
        tv_setting_booth_infor.isFocusable = true
        tv_setting_booth_infor.isFocusableInTouchMode = true
        tv_setting_booth_address.isFocusable = true
        tv_setting_booth_address.isFocusableInTouchMode = true

        tv_setting_booth_name.requestFocus()
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(tv_setting_booth_name, 0)

        view_scrollview.smoothScrollTo(0, 0)

        btn_create_booth.text = "Xong"
    }
}