package ishopgo.com.exhibition.ui.main.map.config.setting

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.map.config.ExpoConfigViewModel
import kotlinx.android.synthetic.main.fragment_config_expo_setting.*

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoSettingFragment : BaseFragment() {

    private lateinit var viewModel: ExpoConfigViewModel
    private var selectedUri: Uri? = null
    private lateinit var config: ExpoConfig

    companion object {

        fun newInstance(params: Bundle): ExpoSettingFragment {
            val fragment = ExpoSettingFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_config_expo_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        if (json != null) {
            config = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
            Glide.with(view.context)
                    .load(config.map)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image)

            view_booth_count.editText?.setText(config.count?.toString())
        }

        tv_edit_image.setOnClickListener {
            launchPickPhotoIntent()
        }
        view_submit.setOnClickListener {
            if (checkRequiredFields()) {
                toast("Chưa điền số gian hàng")
                return@setOnClickListener
            } else {
                val boothCount = view_booth_count.editText?.text?.toString() ?: ""
                val newBoothCount = boothCount.toInt()
                if (newBoothCount < config.count ?: 0) {
                    MaterialDialog.Builder(requireContext())
                            .content("Bạn đang cài đặt số gian hàng thấp hơn số gian hiện tại. Các cài đặt cũ có thể bị mất. \nXác nhận thay đổi ?")
                            .positiveText("OK")
                            .onPositive { _, _ ->  viewModel.settingExpo(config.id!!, selectedUri, newBoothCount) }
                            .negativeText("Huỷ")
                            .show()
                }
                else {
                    viewModel.settingExpo(config.id!!, selectedUri, newBoothCount)
                }

            }

        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(ExpoConfigViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.settingSuccess.observe(this, Observer { a ->
            a?.let {
                toast("Sửa thành công")
                Navigation.findNavController(view_submit).navigateUp()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            selectedUri = data.data

            if (Toolbox.exceedSize(context!!, selectedUri!!, (15 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 15 MB. Hãy chọn file khác.")
                return
            }

            Glide.with(requireContext())
                    .load(selectedUri)
                    .into(view_image)
        }


    }

    private fun checkRequiredFields(): Boolean {
        val boothCount = view_booth_count.editText?.text?.toString() ?: ""

        return boothCount.isBlank()
    }

}