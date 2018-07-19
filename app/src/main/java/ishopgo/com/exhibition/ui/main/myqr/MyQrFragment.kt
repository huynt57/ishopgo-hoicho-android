package ishopgo.com.exhibition.ui.main.myqr

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_myqr.*
import java.io.*

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrFragment : BaseFragment() {

    private lateinit var viewModel: MyQrViewModel
    private lateinit var currentBooth: Booth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myqr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MyQrViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.booth.observe(this, Observer { url ->
            url?.let { booth ->
                currentBooth = booth
                //                view_qrcode.setImageBitmap(QRCode.from("http://hoicho.ishopgo.com/gian-hang/gian-hang-17365?booth=17365").withSize(300, 300).bitmap())
                Glide.with(view_qrcode.context)
                        .asBitmap()
                        .load(booth.qrcode)
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
                                            storeImage(booth.name ?: "unknown")

                                    }

                                return false
                            }
                        })
                        .into(view_qrcode)

                view_booth_name.text = booth.name
            }
        })


        viewModel.loadQrCode()
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
            requireContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath)))
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
                storeImage(currentBooth.name ?: "unknown")
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

}