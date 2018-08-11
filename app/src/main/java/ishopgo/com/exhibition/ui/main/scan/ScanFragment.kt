package ishopgo.com.exhibition.ui.main.scan

import android.Manifest
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.update.IcheckUpdateProductActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import kotlinx.android.synthetic.main.fragment_scan.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanFragment : BaseFragment(), BarcodeCallback {
    companion object {
        const val TAG = "ScanFragment"
    }

    private val capture: CaptureManager? = null
    private lateinit var beepManager: BeepManager
    private lateinit var viewModel: ScanViewModel
    private var isInitBarCodeScanner = false

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
        if (isInitBarCodeScanner)
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

    private fun requestCameraPermission() {
        activity?.let {
            view_request_camera_permission.visibility = View.VISIBLE
            view_notice_permission.text = "Quét mã vạch, mã Qr Code để kiểm tra nguồn gốc sản phẩm và phát hiện hàng giả"
            view_request_camera_permission.setOnClickListener {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), Const.RequestCode.CAMERA_PERMISSION)
            }
        }
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
                showProduct(it)
            }
        })
        viewModel.totalNoResult.observe(this, Observer { a ->
            a?.let {
                showDialogNoResult(it)
                zxing_barcode_scanner.decodeSingle(this@ScanFragment)
            }
        })

        activity?.let {
            beepManager = BeepManager(it)
            beepManager.isBeepEnabled = true
            beepManager.isVibrateEnabled = true
        }

    }

    private fun showDialogNoResult(code: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thông báo")
                    .content("Không tìm thấy thông tin sản phẩm, bạn có muốn đóng góp thông tin sản phẩm này không?")
                    .positiveText("Đồng ý")
                    .onPositive { dialog, _ ->
                        val intent = Intent(context, IcheckUpdateProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, code)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ bỏ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            dialog.show()
        }
    }

    private fun requestIcheckProduct(qrCode: String?) {
        qrCode?.let {
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrCode))
            if (context != null && intent.resolveActivity(context!!.packageManager) != null)
                startActivity(intent)

            zxing_barcode_scanner.decodeSingle(this)
            return
        }

        requestIcheckProduct(qrCode)

//        val uri = Uri.parse(qrCode)
//        val boothId = uri.getQueryParameter("booth")
//        if (boothId != null && boothId.isNotBlank()) {
//            openShopDetail(boothId.toLong())
//        } else
//            toast("Không hợp lệ")
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
//        Log.d(TAG, "    possibleResultPoints: resultPoints = [${resultPoints}]")
    }

}