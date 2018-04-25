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
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_scan.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanFragment : BaseFragment(), BarcodeCallback {
    companion object {
        private val TAG = "ScanFragment"
    }

    private val capture: CaptureManager? = null
    private lateinit var beepManager: BeepManager
    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent()
        intent.putExtra(Intents.Scan.FORMATS, String.format("%s, %s", Intents.Scan.ONE_D_MODE, Intents.Scan.QR_CODE_MODE))
        intent.putExtra(Intents.Scan.CAMERA_ID, Camera.CameraInfo.CAMERA_FACING_BACK)
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "")
        zxing_barcode_scanner.initializeFromIntent(intent)
    }


    override fun onPause() {
        super.onPause()
        pauseCamera()
    }

    override fun onResume() {
        super.onResume()

        context?.let {
            if (hasCameraPermission(it)) {
                view_request_camera_permission.visibility = View.GONE
                resumeCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    fun pauseCamera() {
        zxing_barcode_scanner.post { zxing_barcode_scanner.pauseAndWait() }
    }

    fun resumeCamera() {
        zxing_barcode_scanner.post {
            zxing_barcode_scanner.decodeSingle(this)
            zxing_barcode_scanner.resume()
        }
    }

    private fun hasCameraPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        activity?.let {
            if (ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)) {
                MaterialDialog.Builder(it)
                        .title("Chú ý")
                        .positiveText("OK")
                        .content("Quét mã vạch cần quyền truy cập camera!")
                        .onPositive { _, _ ->
                            requestPermissions(arrayOf(Manifest.permission.CAMERA), Const.RequestCode.CAMERA_PERMISSION)
                        }
                        .show()
            } else {
                view_request_camera_permission.visibility = View.VISIBLE
                view_request_camera_permission.setOnClickListener {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", it.context.packageName, null))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Const.RequestCode.CAMERA_PERMISSION) {
            val hasCallPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (hasCallPermission) {
                view_request_camera_permission.visibility = View.GONE
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

        activity?.let {
            beepManager = BeepManager(it)
            beepManager.isBeepEnabled = true
            beepManager.isVibrateEnabled = true
        }

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
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        Log.d(TAG, "possibleResultPoints: resultPoints = [${resultPoints}]")
    }

}