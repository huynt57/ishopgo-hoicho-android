package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.product.detail.DiaryProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportEditText
import kotlinx.android.synthetic.main.fragment_product_diary_add.*

class ProductDiaryAddFragment : BaseFragment(), LocationListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_diary_add, container, false)
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

    companion object {
        const val TAG = "ProductDiaryAddFragment"
        const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 100
        const val IMAGE_ADD = 0
        const val IMAGE_DELETE = 2

        fun newInstance(params: Bundle): ProductDiaryAddFragment {
            val fragment = ProductDiaryAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var data = ProductDetail()
    private lateinit var viewModel: ProductDetailViewModel
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAddAdapter()
    private var adapterCodeNoStamp = CodeNoStampAdapter()
    private lateinit var viewModelDiary: DiaryProductViewModel
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var locationManager: LocationManager? = null

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

//        tv_add_image.setOnClickListener { launchPickPhotoIntent() }

        btn_add_diary.setOnClickListener {
            if (isRequiredFieldsValid(edit_title.text.toString(), edit_content.text.toString(), edit_code.text.toString())) {
                showProgressDialog()
                val stampCode = if (data.stamp != null) data.stamp!!.code ?: "" else ""
                viewModel.createProductDiary(data.id, edit_title.text.toString(), edit_content.text.toString(), postMedias, stampCode, lat.toString(), lng.toString(), edit_code.text.toString())
            }
        }

        Glide.with(context).load(data.image)
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(img_product)
        tv_product.text = data.name
        tv_product_price.text = data.price.asMoney()
        tv_product_code.text = data.code
        if (data.stamp != null)
            edit_code.setText(data.stamp!!.code ?: "")

        edit_code.setOnClickListener {
            getCodeNoStamp(edit_code)
        }

        setupImageRecycleview()
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


            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            textInputLayout.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterCodeNoStamp

            adapterCodeNoStamp.listener = object : ClickableAdapter.BaseAdapterAction<CodeNoStamp> {
                override fun click(position: Int, data: CodeNoStamp, code: Int) {
                        view.setText(data.code ?: "")
                        view.error = null
                        dialog.dismiss()
                }
            }
            dialog.show()
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelDiary = obtainViewModel(DiaryProductViewModel::class.java, true)
        viewModelDiary.openDiaryTabFragment(false)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.createProductDiary.observe(this, Observer { p ->
            p?.let {
                toast("Tạo nhật ký sản xuất thành công")
                viewModelDiary.createDiarySusscess()
                hideProgressDialog()
                hideKeyboard()
                activity?.onBackPressed()
            }
        })

        viewModel.getCodeNoStamp.observe(this, Observer { p ->
            p?.let {
                adapterCodeNoStamp.replaceAll(it)
            }
        })

        viewModel.getCodeNoStamp(data.id)
    }

    private fun isRequiredFieldsValid(title: String, content: String, code: String): Boolean {
        if (code.trim().isEmpty()) {
            toast("Mã lô tem không được để trống")
            return false
        }
        if (title.trim().isEmpty()) {
            toast("Tiêu đề không được để trống")
            return false
        }
        if (content.trim().isEmpty()) {
            toast("Nội dung không được để trống")
            return false
        }
        return true
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_image.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            rv_image.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            adapterImages.addData(0, PostMedia())
            rv_image.adapter = adapterImages
            adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    when (code) {
                        IMAGE_DELETE -> {
                            postMedias.remove(data)
                            adapterImages.replaceAll(postMedias)
                            if (postMedias.isNotEmpty())
                                adapterImages.addData(postMedias.size, PostMedia())
                            else adapterImages.addData(0, PostMedia())
                        }
                        IMAGE_ADD -> launchPickPhotoIntent()
                    }
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
            if (postMedias.isNotEmpty())
                adapterImages.addData(postMedias.size, PostMedia())
            else adapterImages.addData(0, PostMedia())
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (locationManager != null) {
                        activity?.let {
                            locationManager!!.requestLocationUpdates(locationServicesEnabled(), 0, 0f, this)
                        }
                    }
                }
                return
            }

        }
    }
}