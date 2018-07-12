package ishopgo.com.exhibition.ui.main.map.qrcode

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.map.ExpoDetailViewModel
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_myqr.*
import com.bumptech.glide.request.target.Target

import java.io.*

class ExpoQrCodeFragment : BaseActionBarFragment() {
    private lateinit var viewModel: ExpoDetailViewModel
    private var fairId = -1L
    private lateinit var currentConfig: ExpoConfig

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_myqr
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        view_qr_label.text = "Mã truy cập hội chợ"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fairId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
        if (fairId == -1L)
            throw RuntimeException("Sai dinh dang")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ExpoDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.expoDetail.observe(this, Observer { url ->
            url?.let { config ->
                currentConfig = config
                Glide.with(view_qrcode.context)
                        .asBitmap()
                        .load(config.qrcodePNG)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                if (isVisible)
                                    view_download.setOnClickListener {
                                        toast("Lỗi khi tải ảnh.")
                                    }

                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                if (isVisible)
                                    view_download.setOnClickListener {
                                        if (!hasCameraPermission(it.context))
                                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.RequestCode.STORAGE_PERMISSION)
                                        else
                                            storeImage(config.name ?: "unknown")

                                    }

                                return false
                            }
                        })
                        .into(view_qrcode)

                view_booth_name.text = config.name
            }
        })


        viewModel.loadExpoDetail(fairId)
    }

    private fun hasCameraPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun storeImage(boothName: String): Boolean {
        view_qr_container.isDrawingCacheEnabled = true
        view_qr_container.buildDrawingCache()

        val drawingCache = view_qr_container.drawingCache

        // get path to external storage (SD card)
        val sdIconStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString())

        // create storage directories, if they don't exist
        if (!sdIconStorageDir.exists())
            sdIconStorageDir.mkdirs()

        try {
            val filePath = sdIconStorageDir.toString() + File.separator + boothName + "_" + System.currentTimeMillis() + ".png"
            val fileOutputStream = FileOutputStream(filePath)
            val bos = BufferedOutputStream(fileOutputStream)
            //Toast.makeText(m_cont, "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            drawingCache.compress(Bitmap.CompressFormat.PNG, 85, bos)
            bos.flush()
            bos.close()
            toast("Lưu thành công\n$filePath")

        } catch (e: FileNotFoundException) {
            toast("Không thành công")
            Log.w("TAG", "Error saving image file: " + e.message)
            return false
        } catch (e: IOException) {
            toast("Không thành công")
            Log.w("TAG", "Error saving image file: " + e.message)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Const.RequestCode.STORAGE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                storeImage(currentConfig.name ?: "unknown")
            } else {
                // permission was not granted
                if (activity == null) {
                    return
                }
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    context?.let {
                        MaterialDialog.Builder(it)
                                .content("Hãy cấp quyền cho ứng dụng sử dụng bộ nhớ trong")
                                .positiveText("Đi tới cài đặt")
                                .onPositive { _, _ ->
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", it.packageName, null))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                                .negativeText("Huỷ")
                                .show()
                    }
            }
        }
    }

    private fun setupToolbars() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }

        toolbar.setCustomTitle("Mã QR của hội chợ")
    }
}