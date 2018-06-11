package ishopgo.com.exhibition.ui.main.myqr

import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_myqr.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrFragment : BaseFragment() {

    private var qrCode = ""
    private lateinit var viewModel: MyQrViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myqr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MyQrViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.qrImage.observe(this, Observer { url ->
            url?.let {
                //                view_qrcode.setImageBitmap(QRCode.from("http://hoicho.ishopgo.com/gian-hang/gian-hang-17365?booth=17365").withSize(300, 300).bitmap())
                Glide.with(view_qrcode.context)
                        .load(it)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_qrcode)

                qrCode = it
            }
        })

        viewModel.loadQrCode()
    }

    fun download() {
        if (qrCode.isBlank()) return

        context?.let { ctx ->
            enqueueDownload(qrCode, true)
//            val photoFile: File? = try {
//                createImageFile()
//            } catch (ex: IOException) {
//                Log.e("Hong", "khong the tao file", ex)
//                null
//            }
//
//            photoFile?.let {
////                val photoURI = FileProvider.getUriForFile(ctx, "ishopgo.com.exhibition", it)
//                Requestmanager
//            }

        }
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

    private fun enqueueDownload(url: String, isForNotification: Boolean): Long {
        var downloadReference: Long = -1
        try {
            val uri = Uri.parse(url)
            val downloadManager = context!!.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val request = DownloadManager.Request(uri)

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setTitle("Expo 360")
            request.setDescription("Đang tải về...")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

            // TODO need to check whether download manager is enable or not (https://stackoverflow.com/questions/21551538/how-to-enable-android-download-manager)
            if (isForNotification) {
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,
                        "photo_qr_" + System.currentTimeMillis() + ".jpg")
            } else {
                request.setDestinationInExternalPublicDir("Expo360", "photo_qr_" + System.currentTimeMillis() + ".jpg")
            }
            request.allowScanningByMediaScanner()

            if (downloadManager != null) {
                downloadReference = downloadManager.enqueue(request)
            }

            toast("Đang tải xuống ...")
        } catch (e: IllegalStateException) {
            toast("loi")
            e.printStackTrace()
        }

        return downloadReference
    }
}