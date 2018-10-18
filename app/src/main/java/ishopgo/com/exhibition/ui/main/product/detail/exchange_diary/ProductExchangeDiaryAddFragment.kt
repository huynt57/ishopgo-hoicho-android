package ishopgo.com.exhibition.ui.main.product.detail.exchange_diary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.CodeNoStamp
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.product.detail.ExchangeDiaryProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.product.detail.diary_product.CodeNoStampAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportEditText
import kotlinx.android.synthetic.main.fragment_product_exchange_diary_add.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProductExchangeDiaryAddFragment : BaseFragment(), LocationListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_exchange_diary_add, container, false)
    }

    override fun onLocationChanged(location: Location?) {
        lat = location?.latitude ?: 0.0
        lng = location?.longitude ?: 0.0
        Log.d("latlng", "$lat,$lng")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    private var data = ProductDetail()
    private lateinit var viewModel: ProductDetailViewModel
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private lateinit var viewModelDiary: ExchangeDiaryProductViewModel
    private var typeGui = 0
    private var typeNhan = 0
    private var idGui = -1L
    private var idNhan = -1L
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var locationManager: LocationManager? = null
    private var adapterCodeNoStamp = CodeNoStampAdapter()

    companion object {
        const val TAG = "ProductExchangeDiary"
        const val PERMISSIONS_REQUEST_CAMERA = 100
        const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 100

        fun newInstance(params: Bundle): ProductExchangeDiaryAddFragment {
            val fragment = ProductExchangeDiaryAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusCheck()

        activity?.let {
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_ACCESS_LOCATION)
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_ACCESS_LOCATION)

            } else {
                if (locationManager != null) {
                    locationManager!!.requestLocationUpdates(locationServicesEnabled(), 0, 0f, this)
                }
            }
        }

        view_add_images.setOnClickListener {
            launchPickPhotoIntent()
        }

        view_camera.setOnClickListener {
            takePhoto()
        }

        btn_add.setOnClickListener {
            if (isRequiredFieldsValid(edit_maLo.text.toString(), edit_tenGiaoDich.text.toString(), edit_noiDung.text.toString())) {
                showProgressDialog()
                viewModel.createExchangeDiary(edit_tenGiaoDich.text.toString(), edit_noiDung.text.toString(), idGui, typeGui.toString(), idNhan, typeNhan.toString(), edit_hsd.text.toString(), lat.toString(), lng.toString(),
                        data.id, edit_soLuong.text.toString(), edit_donVi.text.toString(), edit_maLo.text.toString(), postMedias)
            }
        }

        Glide.with(context).load(data.image)
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(img_product)
        tv_product.text = data.name
        tv_product_price.text = data.price.asMoney()
        tv_product_code.text = data.code

        if (data.stamp != null)
            edit_maLo.setText(data.stamp!!.code ?: "")

        setupImageRecycleview()
    }

    private fun statusCheck() {
        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Định vị của bạn đang tắt, vui lòng mở lại?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý") { _, _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("Huỷ") { dialog, _ ->
                    dialog.cancel()
                }
        val alert = builder.create()
        alert.show()
    }

    private fun locationServicesEnabled(): String {
        val gpsEnabled: Boolean
        val netEnabled: Boolean

        var location = LocationManager.NETWORK_PROVIDER

        try {
            gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            location = if (gpsEnabled)
                LocationManager.GPS_PROVIDER
            else LocationManager.NETWORK_PROVIDER
        } catch (ex: Exception) {
            Log.e(TAG, "Exception gps_enabled")
        }

        try {
            netEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (netEnabled)
                location = if (netEnabled)
                    LocationManager.NETWORK_PROVIDER
                else LocationManager.GPS_PROVIDER
        } catch (ex: Exception) {
            Log.e(TAG, "Exception network_enabled")
        }

        return location
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelDiary = obtainViewModel(ExchangeDiaryProductViewModel::class.java, true)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, android.arch.lifecycle.Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.createExchangeDiarySuccess.observe(this, android.arch.lifecycle.Observer { p ->
            p?.let {
                toast("Tạo nhật ký giao dịch thành công")
                viewModelDiary.createExchangeDiarySusscess()
                hideProgressDialog()
                hideKeyboard()
                activity?.onBackPressed()
            }
        })

        viewModelDiary.resultdBenGiao.observe(this, android.arch.lifecycle.Observer { p ->
            p?.let {
                idGui = it.id
                typeGui = it.type ?: 0
                if (typeGui == 1) edit_benGui.setText(it.boothName ?: "")
                else edit_benGui.setText(it.name ?: "")
            }
        })

        viewModelDiary.resultdBenNhan.observe(this, android.arch.lifecycle.Observer { p ->
            p?.let {
                idNhan = it.id
                typeNhan = it.type ?: 0
                if (typeNhan == 1) edt_benNhan.setText(it.boothName ?: "")
                else edt_benNhan.setText(it.name ?: "")
            }
        })


        viewModel.getCodeNoStamp.observe(this, android.arch.lifecycle.Observer { p ->
            p?.let {
                adapterCodeNoStamp.replaceAll(it)
            }
        })


        edit_benGui.setOnClickListener { showAddExchangDiary(data, true) }
        edt_benNhan.setOnClickListener { showAddExchangDiary(data, false) }

        edit_maLo.setOnClickListener {
            getCodeNoStamp(edit_maLo)
        }

        viewModel.getCodeNoStamp(data.id)
    }

    private fun getCodeNoStamp(view: VectorSupportEditText) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn mã lô tem")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            textInputLayout.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterCodeNoStamp

            adapterCodeNoStamp.listener = object : ClickableAdapter.BaseAdapterAction<CodeNoStamp> {
                override fun click(position: Int, data: CodeNoStamp, code: Int) {
                    context?.let {
                        view.setText(data.code ?: "")
                        view.error = null
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun showAddExchangDiary(product: ProductDetail, statusBGBN: Boolean) {
        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        extra.putBoolean(Const.TransferKey.EXTRA_STATUS_BG_BN, statusBGBN)

        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_productExchangeBGBNFragment, extra)
    }

    private fun isRequiredFieldsValid(maLo: String, name: String, content: String): Boolean {
        if (maLo.trim().isEmpty()) {
            toast("Mã lô không được để trống")
            return false
        }
        if (name.trim().isEmpty()) {
            toast("Tên giao dịch không được để trống")
            return false
        }
        if (content.trim().isEmpty()) {
            toast("Nội dung giao dịch không được để trống")
            return false
        }
        if (postMedias.isEmpty()) {
            toast("Ảnh sản phẩm không được để trống")
            return false
        }
        return true
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_images.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            rv_images.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_images.adapter = adapterImages
            adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMedias.remove(data)
                    if (postMedias.isEmpty()) rv_images.visibility = View.GONE
                    adapterImages.replaceAll(postMedias)
                }
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private var sendingPhotoUri: Uri? = null
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )
    }

    private fun takePhoto() {
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
                    val photoURI = FileProvider.getUriForFile(context!!, getString(R.string.file_provider_authority), it)
                    sendingPhotoUri = photoURI

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, Const.RequestCode.TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data

                postMedias.add(postMedia)

            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages.replaceAll(postMedias)
            rv_images.visibility = View.VISIBLE

        }

        if (requestCode == Const.RequestCode.TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            sendingPhotoUri?.let {
                val postMedia = PostMedia()

                postMedia.uri = it
                postMedias.add(postMedia)
                adapterImages.replaceAll(postMedias)
                rv_images.visibility = View.VISIBLE

            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (locationManager != null) {
                        locationManager!!.requestLocationUpdates(locationServicesEnabled(), 0, 0f, this)
                    }
                }
                return
            }

        }
    }
}