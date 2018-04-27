package ishopgo.com.exhibition.ui.community

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_share_community.*
import android.provider.MediaStore
import java.io.File
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by hoangnh on 4/26/2018.
 */
class CommunityShareFragment : BaseActionBarFragment() {

    private lateinit var viewModel: CommunityViewModel
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private lateinit var adapterImages: ComposingPostMediaAdapter
    private val PERMISSIONS_REQUEST_CAMERA = 100
    private var sendingPhotoUri: Uri? = null

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_share_community
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        img_select_camera.setOnClickListener {
            takePhoto()
        }

        img_select_picture.setOnClickListener {
            launchPickPhotoIntent()
        }

        Glide.with(this).load("http://www.urhobosocialclublagos.com/wp-content/uploads/2017/07/default-avatar-ginger-guy.png")
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(img_share_avatar)

    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chia sẻ bài viết")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_send_24dp)
        toolbar.setRightButtonClickListener {
            if (checkRequireFields(edit_share.text.toString())) {
                showProgressDialog()
                viewModel.sentShareCommunity(edit_share.text.toString(), postMedias)
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    fun takePhoto() {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {

            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA) }

        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(context?.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.e("Hong", "khong the tao file", ex)
                }
                photoFile?.let {
                    val photoURI = FileProvider.getUriForFile(context!!, "ishopgo.com.exhibition", it)
                    sendingPhotoUri = photoURI

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, Const.RequestCode.TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(CommunityViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.sentShareSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đăng thành công")
        })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            Log.d("CommunityShareFragment", data.data.toString())

            if (Toolbox.exceedSize(context, data.data, (20 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 20 MB. Hãy chọn file khác.")
                return
            }

            val postMedia = PostMedia()

            if (postMedias.size == 0) {
                postMedia.uri = data.data
                postMedias.add(postMedia)
            } else {
                for (i in postMedias.indices) {
                    if (postMedias[i].uri!! == data.data) {
                        toast("Ảnh này đã tồn tại, vui lòng chọn ảnh khác.")
                        return
                    } else {
                        postMedia.uri = data.data
                        postMedias.add(postMedia)
                    }
                }
            }

            adapterImages = ComposingPostMediaAdapter(postMedias)
            adapterImages.notifyItemInserted(postMedias.size - 1)
            rv_share_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_share_image.adapter = adapterImages
        }

        if (requestCode == Const.RequestCode.TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            sendingPhotoUri?.let {
                val postMedia = PostMedia()
                if (Toolbox.exceedSize(context, it, (20 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 20 MB. Hãy chọn file khác.")
                    return
                }

                if (postMedias.size == 0) {
                    postMedia.uri = it
                    postMedias.add(postMedia)
                } else {
                    for (i in postMedias.indices) {
                        if (postMedias[i].uri!! == it) {
                            toast("Ảnh này đã tồn tại, vui lòng chọn ảnh khác.")
                            return
                        } else {
                            postMedia.uri = it
                            postMedias.add(postMedia)
                        }
                    }
                }
                adapterImages = ComposingPostMediaAdapter(postMedias)
                adapterImages.notifyItemInserted(postMedias.size - 1)
                rv_share_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rv_share_image.adapter = adapterImages
            }
        }
    }

    private fun checkRequireFields(share: String): Boolean {
        if (share.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
    }
}