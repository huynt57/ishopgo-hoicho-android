package ishopgo.com.exhibition.ui.main.configbooth

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var viewModel: ConfigBoothViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_setting_booth.setOnClickListener { launchPickPhotoIntent() }

        btn_create_booth.setOnClickListener {
            if (checkRequireFields(image, tv_setting_booth_name.text.toString())) {
                showProgressDialog()
                viewModel.editConfigBooth(tv_setting_booth_name.text.toString(), tv_setting_booth_hotline.text.toString(),
                        tv_setting_booth_infor.text.toString(), tv_setting_booth_introduction.text.toString(), tv_setting_booth_address.text.toString(), image)
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
                toast("Tạo thành thành công")
            }
        })

        viewModel.getConfigBooth.observe(this, Observer {
            it?.let {
                tv_setting_booth_name.setText(it.provideName())
                tv_setting_booth_hotline.setText(it.provideHotline())
                tv_setting_booth_introduction.setText(it.provideIntroduction())
                tv_setting_booth_infor.setText(it.provideInfo())
                tv_setting_booth_address.setText(it.provideAddress())
                image = it.provideBanner()

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

    private fun checkRequireFields(image: String, name: String): Boolean {
        if (image.trim().isEmpty()) {
            toast("Ảnh sản phẩm không được để trống")
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Tên gian hàng không được để trống")
            tv_setting_booth_name.error = "Trường này còn trống"
            return false
        }
        return true
    }
}