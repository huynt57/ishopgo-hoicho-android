package ishopgo.com.exhibition.ui.main.notification.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_notification_add.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationAddFragment : BaseFragment() {
    companion object {
        val TYPE_PERSONAL = 1
        val TYPE_ALL = 0
    }

    var send = HashMap<Int, String>()
    var requestSendType = TYPE_ALL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_image.setOnClickListener { launchPickPhotoIntent() }

        view_create.setOnClickListener {
            //            if (checkRequireFields(image, tv_setting_booth_name.text.toString())) {
//                showProgressDialog()
//                viewModel.editConfigBooth(tv_setting_booth_name.text.toString(), tv_setting_booth_hotline.text.toString(),
//                        tv_setting_booth_infor.text.toString(), tv_setting_booth_introduction.text.toString(), tv_setting_booth_address.text.toString(), image)
//            }
        }

        send[TYPE_ALL] = "Tất cả thành viên của bạn"
        send[TYPE_PERSONAL] = "Cá nhân"

        requestSendType = TYPE_ALL
        when (requestSendType) {
            TYPE_ALL -> view_container_add_receiver.visibility = View.GONE
            TYPE_PERSONAL -> view_container_add_receiver.visibility = View.VISIBLE
        }

        view_send_type.setText(send[requestSendType])
        view_send_type.setOnClickListener {
            context?.let {
                MaterialDialog.Builder(it)
                        .title("Hình thức gửi")
                        .items(send.values)
                        .itemsCallback { _, _, _, text ->

                            for ((k, v) in send) {
                                if (v.equals(text.toString(), true)) {
                                    requestSendType = k
                                    break
                                }
                            }
                            view_send_type.setText(text)

                            when (requestSendType) {
                                TYPE_ALL -> view_container_add_receiver.visibility = View.GONE
                                TYPE_PERSONAL -> view_container_add_receiver.visibility = View.VISIBLE
                            }
                        }
                        .show()
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private var image = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context!!, data.data, (2 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(view_image)
        }
    }
}