package ishopgo.com.exhibition.ui.main.map.config.edit

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.map.config.ExpoConfigViewModel
import kotlinx.android.synthetic.main.fragment_config_expo_edit.*
import java.text.SimpleDateFormat

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoEditFragment : BaseFragment() {

    private lateinit var viewModel: ExpoConfigViewModel
    private var selectedUri: Uri? = null
    private lateinit var expoConfig: ExpoConfig
    private val timeFormat = "HH:mm dd/MM/yyyy"
    private val displayDateFormat = SimpleDateFormat(timeFormat, Toolbox.LOCALE_VN)

    companion object {

        fun newInstance(params: Bundle): ExpoEditFragment {
            val fragment = ExpoEditFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_config_expo_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        expoConfig = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
        Glide.with(view.context)
                .load(expoConfig.image)
                .into(view_avatar)
        view_name.editText?.setText(expoConfig.name)
        view_start.editText?.setText(displayDateFormat.format(Toolbox.apiDateFormat.parse(expoConfig.startTime)))
        view_end.editText?.setText(displayDateFormat.format(Toolbox.apiDateFormat.parse(expoConfig.endTime)))
        view_address.editText?.setText(expoConfig.address)
        view_description.editText?.setText(expoConfig.description?.asHtml())
        tv_price.setText(expoConfig.price.toString())

        view_avatar.setOnClickListener {
            launchPickPhotoIntent()
        }
        tv_edit_image.setOnClickListener {
            launchPickPhotoIntent()
        }
        view_submit.setOnClickListener {
            val name = view_name.editText?.text.toString()
            val startTime = view_start.editText?.text.toString()
            val endTime = view_end.editText?.text.toString()
            val address = view_address.editText?.text.toString()
            val description = view_description.editText?.text.toString()
            val price = tv_price.money ?: 0L

            if (name.isBlank() || address.isBlank() || description.isBlank()) {
                toast("Chưa điền hết thông tin")
                return@setOnClickListener
            }

            if (!isTimeValid(startTime)) {
                toast("Thời gian bắt đầu không hợp lệ")
                return@setOnClickListener
            }

            if (!isTimeValid(endTime)) {
                toast("Thời gian kết thúc không hợp lệ")
                return@setOnClickListener
            }

            viewModel.editExpo(expoConfig.id!!, selectedUri, name, startTime, endTime, address, description, price)

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
        viewModel.editSuccess.observe(this, Observer { a ->
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

            if (Toolbox.exceedSize(context!!, selectedUri!!, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            Glide.with(requireContext())
                    .load(selectedUri)
                    .into(view_avatar)
        }


    }

    private fun isTimeValid(time: String?): Boolean {
        if (time.isNullOrBlank()) return false

        return try {
            displayDateFormat.parse(time); true
        } catch (e: Exception) {
            false
        }
    }

}