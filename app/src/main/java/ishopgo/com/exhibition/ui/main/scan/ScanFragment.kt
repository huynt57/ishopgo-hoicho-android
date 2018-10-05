package ishopgo.com.exhibition.ui.main.scan

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.text.format.Time
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.update.IcheckUpdateProductActivity
import ishopgo.com.exhibition.ui.main.scan.history.HistoryScanActivity
import kotlinx.android.synthetic.main.fragment_scan.*
import java.util.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanFragment : BaseFragment(), BarcodeCallback {
    companion object {
        const val TAG = "ScanFragment"
    }

    private lateinit var beepManager: BeepManager
    private lateinit var viewModel: ScanViewModel
    private var isInitBarCodeScanner = false
    private var isFlash = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")

        if (isInitBarCodeScanner)
            pauseCamera()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")

        if (isInitBarCodeScanner)
            context?.let {
                if (hasCameraPermission(it)) {
                    view_request_camera_permission.visibility = View.GONE
                    view_notice_permission.visibility = View.VISIBLE
                    view_notice_permission.text = "Quét mã vạch, mã Qr Code để kiểm tra nguồn gốc sản phẩm và phát hiện hàng giả"
                    resumeCamera()
                } else {
                    requestCameraPermission()
                }
            }
    }

    private fun initBarcodeScanner() {
        if (!isInitBarCodeScanner) {
            Log.d(TAG, "init barcode scanner")
            val intent = Intent()
            intent.putExtra(Intents.Scan.FORMATS, String.format("%s, %s", Intents.Scan.ONE_D_MODE, Intents.Scan.QR_CODE_MODE))
            intent.putExtra(Intents.Scan.CAMERA_ID, Camera.CameraInfo.CAMERA_FACING_BACK)
            intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "")
            zxing_barcode_scanner.initializeFromIntent(intent)
            zxing_barcode_scanner.decodeSingle(this)
            zxing_barcode_scanner.resume()
            isInitBarCodeScanner = true

            context?.let {
                val hasCameraPermission = hasCameraPermission(it)
                view_request_camera_permission.visibility = if (hasCameraPermission) View.GONE else View.VISIBLE
                view_notice_permission.text = if (hasCameraPermission) "Quét mã vạch, mã Qr Code để kiểm tra nguồn gốc sản phẩm và phát hiện hàng giả"
                else "Quét mã vạch cần quyền truy cập camera"
                view_request_camera_permission.setOnClickListener {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), Const.RequestCode.CAMERA_PERMISSION)
                }
            }
        }
    }

    fun pauseCamera() {
        Log.d(TAG, "pauseCamera: ")
        if (isInitBarCodeScanner && zxing_barcode_scanner != null)
            zxing_barcode_scanner.post { zxing_barcode_scanner.pauseAndWait() }
        else {
            initBarcodeScanner()
        }
    }

    fun resumeCamera() {
        Log.d(TAG, "resumeCamera: ")
        if (isInitBarCodeScanner)
            zxing_barcode_scanner.post {
                zxing_barcode_scanner.decodeSingle(this)
                zxing_barcode_scanner.resume()
            }
        else {
            initBarcodeScanner()
        }
    }

    private fun hasCameraPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("SetTextI18n")
    private fun requestCameraPermission() {
        activity?.let {
            view_request_camera_permission.visibility = View.VISIBLE
            view_notice_permission.text = "Quét mã vạch, mã Qr Code để kiểm tra nguồn gốc sản phẩm và phát hiện hàng giả"
            view_request_camera_permission.setOnClickListener {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), Const.RequestCode.CAMERA_PERMISSION)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_contact.text = "Không quét được mã sản phẩm thì vui lòng xem xét kỹ sản phẩm, cân nhắc trước khi mua."
        view_flash.setOnClickListener {
            if (!isFlash) {
                if (isInitBarCodeScanner && zxing_barcode_scanner != null) {
                    Glide.with(context).load(R.drawable.ic_flash_off).into(view_flash)
                    isFlash = true
                    zxing_barcode_scanner.setTorchOn()
                } else toast("Có lỗi xảy ra")
            } else {
                if (isInitBarCodeScanner && zxing_barcode_scanner != null) {
                    Glide.with(context).load(R.drawable.ic_flash_on).into(view_flash)
                    isFlash = false
                    zxing_barcode_scanner.setTorchOff()
                } else toast("Có lỗi xảy ra")
            }
        }
        frame_history.setOnClickListener {
            if (isInitBarCodeScanner)
                pauseCamera()
            val intent = Intent(context, HistoryScanActivity::class.java)
            startActivity(intent)
        }
//        tv_contact.setPhone("Không quét được mã xin vui lòng gọi: 0985771133","0985771133")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Const.RequestCode.CAMERA_PERMISSION) {
            val hasCallPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (hasCallPermission) {
                view_request_camera_permission.visibility = View.GONE
                view_notice_permission.text = "Quét mã vạch, mã Qr Code để kiểm tra nguồn gốc sản phẩm và phát hiện hàng giả"
                resumeCamera()
            } else {
                context?.let {
                    MaterialDialog.Builder(it)
                            .content("Hãy cấp quyền cho ứng dụng sử dụng camera")
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ScanViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.scanIcheckResult.observe(this, Observer { icheckProduct ->
            icheckProduct?.let {
                hideProgressDialog()
                showProduct(it)
                saveBarCode(icheckProduct.code, icheckProduct)
            }
        })
        viewModel.totalNoResult.observe(this, Observer { a ->
            a?.let {
                hideProgressDialog()
                showDialogNoResult(it)
                zxing_barcode_scanner.decodeSingle(this@ScanFragment)
                saveBarCode(a, null)
            }
        })

        viewModel.linkQRCode.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                openDeeplink(it)
//                zxing_barcode_scanner.decodeSingle(this)
            }
        })

        activity?.let {
            beepManager = BeepManager(it)
            beepManager.isBeepEnabled = true
            beepManager.isVibrateEnabled = true
        }

    }

    private fun showDialogNoResult(code: String) {
        if (isInitBarCodeScanner)
            pauseCamera()

        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thông báo")
                    .content("Không tìm thấy thông tin sản phẩm trên hệ thống. Bạn vui lòng giúp cộng đồng đóng góp thông tin\n")
                    .positiveText("Đồng ý")
                    .onPositive { dialog, _ ->
                        val intent = Intent(context, IcheckUpdateProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, code)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ bỏ")
                    .onNegative { dialog, _ ->
                        resumeCamera()
                        dialog.dismiss()
                    }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            dialog.show()
        }
    }

    private fun requestIcheckProduct(qrCode: String?) {
        qrCode?.let {
            showProgressDialog()
            viewModel.loadIcheckProduct(it)
        }
    }

    private fun showProduct(data: IcheckProduct?) {
        val intent = Intent(context, IcheckProductActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast("Canceled")
            } else {
                toast("Scanned: " + result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun barcodeResult(result: BarcodeResult?) {
        if (result == null || TextUtils.isEmpty(result.text)) return
        beepManager.playBeepSoundAndVibrate()
        processData(result.text)
    }

    private fun processData(qrCode: String?) {
        Log.d(TAG, "processData: qrCode = [${qrCode}]")

        if (TextUtils.isEmpty(qrCode)) return

        if (qrCode!!.toLowerCase().startsWith("http")) {
            val code = qrCode.replace("http://${resources.getString(R.string.app_host)}/check/", "")
            Log.d(TAG, "processData: Code = [${code}]")
            if (code != qrCode) {
                showProgressDialog()
                val requestLinkQrCode = String.format("http://ishopgo.expo360.vn/url/%s", code)
                viewModel.getLinkQRCode(requestLinkQrCode)
            } else {
                showProgressDialog()
                if (!qrCode.toLowerCase().startsWith("https://bbp88.app.goo.gl") && !qrCode.toLowerCase().startsWith("https://yf5kt.app.goo.gl")
                        && !qrCode.toLowerCase().startsWith("https://expo360.page.link") && !qrCode.toLowerCase().startsWith("https://hangviet360.page.link")
                        && !qrCode.toLowerCase().startsWith("https://farm360.page.link") && !qrCode.toLowerCase().startsWith("https://hoptacxa.page.link")
                        && !qrCode.toLowerCase().startsWith("https://icheckexpo.page.link") && !qrCode.toLowerCase().startsWith("https://htxnongnghiep.page.link")) {
                    saveQrCode(qrCode)
                }
                openDeeplink(qrCode)
            }

            return
        }

        if (android.text.TextUtils.isDigitsOnly(qrCode)) {
            requestIcheckProduct(qrCode)
            return
        }

        val intent = Intent(context, ScanResultActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_CONTENT, qrCode)
        startActivity(intent)

//        val uri = Uri.parse(qrCode)
//        val boothId = uri.getQueryParameter("booth")
//        if (boothId != null && boothId.isNotBlank()) {
//            openShopDetail(boothId.toLong())
//        } else
//            toast("Không hợp lệ")
    }

    private fun openDeeplink(code: String) {
        hideProgressDialog()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(code))
        if (context != null && intent.resolveActivity(context!!.packageManager) != null)
            startActivity(intent)
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
//        Log.d(TAG, "    possibleResultPoints: resultPoints = [${resultPoints}]")
    }

    private fun saveQrCode(link: String) {
        val listHistoryScan = mutableListOf<JsonElement>()
        if (UserDataManager.currentQrCode.isNotEmpty()) {
            val listQrCode = Toolbox.gson.fromJson<MutableList<HistoryScan>>(UserDataManager.currentQrCode, object : TypeToken<MutableList<HistoryScan>>() {}.type)
            for (i in listQrCode.indices)
                listHistoryScan.add(Toolbox.gson.toJsonTree(listQrCode[i]))
        }

        val historyScan = HistoryScan()
        historyScan.link = link
        historyScan.time = Toolbox.getDateTimeCurrent()

        listHistoryScan.add(0, Toolbox.gson.toJsonTree(historyScan))
        UserDataManager.currentQrCode = listHistoryScan.toString()
    }

    private fun saveBarCode(code: String?, icheckProduct: IcheckProduct?) {
        val listHistoryBarCode = mutableListOf<JsonElement>()
        if (UserDataManager.currentBarCode.isNotEmpty()) {
            val listBarCode = Toolbox.gson.fromJson<MutableList<HistoryScan>>(UserDataManager.currentBarCode, object : TypeToken<MutableList<HistoryScan>>() {}.type)
            for (i in listBarCode.indices)
                listHistoryBarCode.add(Toolbox.gson.toJsonTree(listBarCode[i]))
        }

        val historyScan = HistoryScan()
        historyScan.code = code ?: ""
        historyScan.time = Toolbox.getDateTimeCurrent()

        if (icheckProduct != null) {
            historyScan.icheckProduct = icheckProduct
            historyScan.productId = icheckProduct.id
            historyScan.productName = icheckProduct.productName ?: ""
            val linkImage = icheckProduct.imageDefault ?: ""
            historyScan.productImage = if (linkImage.toLowerCase().startsWith("http")) linkImage else "http://ucontent.icheck.vn/" + linkImage + "_medium.jpg"
            historyScan.productPrice = if (icheckProduct.priceDefault == 0.0) {
                "Liên hệ"
            } else icheckProduct.priceDefault.asMoney()
        }
        listHistoryBarCode.add(0, Toolbox.gson.toJsonTree(historyScan))
        UserDataManager.currentBarCode = listHistoryBarCode.toString()
    }
}