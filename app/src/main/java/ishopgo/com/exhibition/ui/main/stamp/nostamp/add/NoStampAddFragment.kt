package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.domain.response.ValueSync
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.productmanager.add.BoothAdapter
import ishopgo.com.exhibition.ui.main.productmanager.add.BrandsAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.main.stamp.nostamp.edit.TrackingAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.edit.map.NoStampMapActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.content_no_stamp_add.*
import kotlinx.android.synthetic.main.content_title_map_marker.view.*
import java.io.IOException
import java.lang.reflect.InvocationTargetException

class NoStampAddFragment : BaseFragment(), OnMapReadyCallback {

    private var stampId = 0L
    private var adapter = TrackingAdapter()
    private lateinit var viewModel: NoStampViewModel
    private val adapterBrands = BrandsAdapter()
    private val adapterBooth = BoothAdapter()
    private val adapterTitle = TitleAdapter()
    private val adapterBoothCurrent = BoothAdapter()
    private var reloadBrands = false
    private var reloadProvider = false
    private var thuongHieuId: Long = 0L
    private var gianHangId: Long = 0L
    private var productId: Long = 0L
    private var listTracking = mutableListOf<Tracking>()
    private var mMap: GoogleMap? = null
    private val ZOOM_LEVEL = 15f
    private var FROM = LatLng(0.0, 0.0)
    private var waypoint: ArrayList<LatLng> = ArrayList()
    private var END = LatLng(0.0, 0.0)
    private lateinit var mapFragment: SupportMapFragment
    private var countLoop = 0


    val title = mutableListOf<String>(
            "Điểm bán lẻ",
            "Nhà phân phối",
            "Nhà sản xuất",
            "Nhà nhập khẩu",
            "Trang trại",
            "Nhà vườn",
            "Hộ nông dân",
            "Cơ quan kiểm dịch",
            "Nhà máy sản xuất",
            "Nhà máy chế biến",
            "Khu sơ chế",
            "Nhà sản xuất"
    )

    companion object {

        fun newInstance(params: Bundle): NoStampAddFragment {
            val fragment = NoStampAddFragment()
            fragment.arguments = params

            return fragment
        }

        const val ADD_TRACKING = true
        const val BOOTH_CURRENT = "1"
        const val BOOTH_SYSTEM = "2"
        const val BOOTH_SHOP = "3"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_add, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_show_map.setOnClickListener {
            val intent = Intent(context, NoStampMapActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(listTracking))
            startActivity(intent)
        }
        adapterTitle.replaceAll(title)
        edit_no_stamp_thuongHieu.setOnClickListener { getBrands(edit_no_stamp_thuongHieu) }
        edit_no_stamp_gianHang.setOnClickListener { getBooth(edit_no_stamp_gianHang) }
        edit_no_stamp_sanPham.setOnClickListener { viewModel.openSearchProduct(stampId) }
        edit_no_stamp.setOnClickListener { viewModel.getGenerateStamp() }
        img_add_maker.setOnClickListener { showDialogAddMaker(ADD_TRACKING, "", null) }
        rv_tracking.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_tracking.layoutManager = layoutManager
        layoutManager.isAutoMeasureEnabled = true
        rv_tracking.isNestedScrollingEnabled = false
        rv_tracking.setHasFixedSize(false)
        rv_tracking.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))


        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Tracking> {
            override fun click(position: Int, data: Tracking, code: Int) {
                context?.let {
                    showDialogClickTrackingAdapter(data, position)
                }
            }
        }

        btn_assign_no_stamp.setOnClickListener {
            if (isRequiredFieldsValid(edit_no_stamp.text.toString(), edit_no_stamp_thuongHieu.text.toString(), edit_no_stamp_gianHang.text.toString(), edit_no_stamp_sanPham.text.toString(), edit_no_stamp_gioiHan.text.toString())) {
                showProgressDialog()
                viewModel.addNoStampDetail(productId, edit_no_stamp.text.toString(), edit_no_stamp_gioiHan.text.toString(), listTracking, edit_no_stamp_ngaySX.text.toString(),
                        edit_no_stamp_HSD.text.toString(), edit_no_stamp_soLuong.text.toString())
            }
        }
    }

    private fun isRequiredFieldsValid(maTem: String, thuongHieu: String, gianHang: String, sanPham: String, gioiHan: String): Boolean {
        if (maTem.trim().isEmpty()) {
            toast("Mã lô không được để trống")
            edit_no_stamp.error = "Trường này còn trống"
            return false
        }

//        if (thuongHieu.trim().isEmpty()) {
//            toast("Thương hiệu không được để trống")
//            edit_no_stamp_thuongHieu.error = "Trường này còn trống"
//            return false
//        }
//
//        if (gianHang.trim().isEmpty()) {
//            toast("Gian hàng không được để trống")
//            edit_no_stamp_gianHang.error = "Trường này còn trống"
//            return false
//        }

        if (sanPham.trim().isEmpty()) {
            toast("Sản phẩm cần gán không được để trống")
            edit_no_stamp_sanPham.error = "Trường này còn trống"
            return false
        }

        if (gioiHan.trim().isEmpty()) {
            toast("Giới hạn cảnh báo không được để trống")
            edit_no_stamp_gioiHan.error = "Trường này còn trống"
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(NoStampViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { it ->
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.generateStamp.observe(this, Observer { p ->
            p?.let {
                edit_no_stamp.setText(it)
            }
        })
//        viewModel.getDataNoStampCreated.observe(this, Observer { p ->
//            p?.let {
//                edit_no_stamp_serial.setText(it.serialNumberPrefix)
//                tv_no_stamp_count_exits.text = "Số lượng tem còn lại: ${it.calculateQuantity ?: 0}"
//                countStampExist = it.calculateQuantity ?: 0
//                serialNumber = it.serialNumberPrefix ?: ""
//            }
//        })

        viewModel.addNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.dataBrands.observe(this, Observer { p ->
            p.let {
                if (reloadBrands)
                    it?.let { it1 -> adapterBrands.replaceAll(it1) }
                else it?.let { it1 -> adapterBrands.addAll(it1) }

            }
        })

        viewModel.dataBooth.observe(this, Observer { p ->
            p?.let {
                if (reloadProvider) adapterBooth.replaceAll(it) else adapterBooth.addAll(it)
            }
        })

        viewModel.resultProduct.observe(this, Observer { p ->
            p?.let {
                productId = it.id
                edit_no_stamp_sanPham.setText(it.name ?: "")
            }
        })
        viewModel.shopRelates.observe(this, Observer { p ->
            p?.let {
                adapterBoothCurrent.addAll(it)
            }
        })

        viewModel.deleteTrackingSuccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
            }
        })

        viewModel.createTrackingSucccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
                hideProgressDialog()
                toast("Thêm hành trình thành công")
            }
        })

        viewModel.getDataConfigBooth.observe(this, Observer { p ->
            p?.let {
                val booth = BoothManager()
                booth.id = if (UserDataManager.currentBoothId > 0)
                    UserDataManager.currentBoothId
                else UserDataManager.currentUserId

                booth.boothName = it.name ?: ""
                booth.phone = it.hotline ?: ""
                booth.address = it.address ?: ""
                booth.valueType = BOOTH_SHOP
                adapterBoothCurrent.addData(0, booth)
                getLatLngFromAddress(booth, "Nhà sản xuất", null, null)
            }
        })

        reloadBrands = true
        reloadProvider = true

        firstLoadBrand()
        firstLoadProvider()
        if (UserDataManager.currentBoothId > 0)
            viewModel.loadShopRelates(UserDataManager.currentBoothId)
        else viewModel.loadShopRelates(UserDataManager.currentUserId)

        if (UserDataManager.currentType == "Chủ gian hàng") {
            viewModel.getConfigBooth()
        }
    }

    private fun getBrands(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thương hiệu")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edtSearch = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edtSearch.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterBrands
            rvSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreBrands(totalItemsCount)
                }
            })

            adapterBrands.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
                override fun click(position: Int, data: Brand, code: Int) {
                    dialog.dismiss()
                    thuongHieuId = data.id
                    view.text = data.name ?: ""
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    private fun getBooth(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edtSearch = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edtSearch.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterBooth
            rvSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    dialog.dismiss()
                    gianHangId = data.id
                    view.text = data.boothName ?: ""
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    private fun getTitle(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()


            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edtSearch = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edtSearch.visibility = View.GONE

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterTitle
            rvSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterTitle.listener = object : ClickableAdapter.BaseAdapterAction<String> {
                override fun click(position: Int, data: String, code: Int) {
                    dialog.dismiss()
                    view.text = data
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    private fun firstLoadBrand() {
        reloadBrands = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBrand(firstLoad)
    }

    private fun loadMoreBrands(currentCount: Int) {
        reloadBrands = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBrand(loadMore)
    }

    private fun firstLoadProvider() {
        reloadProvider = true
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.getBooth(firstLoad)
    }

    private fun loadMoreProvider(currentCount: Int) {
        reloadProvider = false
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.getBooth(loadMore)
    }

    private fun showDialogAddMaker(type: Boolean, title: String, position: Int?) {
        context?.let { it ->
            val titleDialog = if (type == ADD_TRACKING) "Thêm hành trình" else "Sửa hành trình"
            val dialog = MaterialDialog.Builder(it)
                    .title(titleDialog)
                    .customView(R.layout.dialog_no_stamp_journeys, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()

            val tvAffiliates = dialog.findViewById(R.id.tv_affiliates) as VectorSupportTextView
            tvAffiliates.setOnClickListener {
                getBoothCurrent(type, title, position)
                dialog.dismiss()
            }
            val tvSystem = dialog.findViewById(R.id.tv_system) as VectorSupportTextView
            tvSystem.setOnClickListener {
                getBoothSystem(type, title, position)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun showDialogClickTrackingAdapter(data: Tracking, position: Int) {
        context?.let { it ->
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_no_stamp_tracking, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tvViewTracking = dialog.findViewById(R.id.tv_viewTracking) as VectorSupportTextView
            tvViewTracking.setOnClickListener {
                val latLng = LatLng(data.lat, data.lng)
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
                dialog.dismiss()
            }
            val tvEditTracking = dialog.findViewById(R.id.tv_editTracking) as VectorSupportTextView
            tvEditTracking.setOnClickListener {
                showDialogAddMaker(!ADD_TRACKING, data.title ?: "", position)
                dialog.dismiss()
            }

            val tvDeleteTracking = dialog.findViewById(R.id.tv_deleteTracking) as VectorSupportTextView
            tvDeleteTracking.setOnClickListener {
                dialogDeleteTracking(position)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun dialogDeleteTracking(position: Int) {
        MaterialDialog.Builder(requireContext())
                .content("Bạn có muốn xoá hành trình này không?")
                .positiveText("Có")
                .onPositive { dialog, _ ->
                    listTracking.removeAt(position)
                    toast("Xoá thành công")
                    dialog.dismiss()
                }
                .negativeText("Không")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun getBoothSystem(type: Boolean, title: String, i: Int?) {
        context?.let { it ->
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            val edtSearch = dialog.findViewById(R.id.edt_search) as TextInputEditText
            textInputLayout.visibility = View.VISIBLE
            textInputLayout.hint = "Tiêu đề danh mục"
            edtSearch.isFocusable = false
            edtSearch.isFocusableInTouchMode = false
            edtSearch.setOnClickListener { getTitle(edtSearch) }
            edtSearch.setText(if (type == ADD_TRACKING) "Điểm bán lẻ" else title)

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterBooth
            rvSearch.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    if (type == ADD_TRACKING)
                        getLatLngFromAddress(data, edtSearch.text.toString(), null, BOOTH_SYSTEM)
                    else getLatLngFromAddress(data, edtSearch.text.toString(), i, BOOTH_SYSTEM)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun getBoothCurrent(type: Boolean, title: String, i: Int?) {
        context?.let { it ->
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rvSearch = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            val edtSearch = dialog.findViewById(R.id.edt_search) as TextInputEditText
            textInputLayout.visibility = View.VISIBLE
            textInputLayout.hint = "Tiêu đề danh mục"
            edtSearch.isFocusable = false
            edtSearch.isFocusableInTouchMode = false
            edtSearch.setOnClickListener { getTitle(edtSearch) }
            edtSearch.setText(if (type == ADD_TRACKING) "Điểm bán lẻ" else title)

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rvSearch.layoutManager = layoutManager

            rvSearch.adapter = adapterBoothCurrent

            adapterBoothCurrent.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        if (type == ADD_TRACKING)
                            getLatLngFromAddress(data, edtSearch.text.toString(), null, BOOTH_CURRENT)
                        else getLatLngFromAddress(data, edtSearch.text.toString(), i, BOOTH_CURRENT)

                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getLatLngFromAddress(data: BoothManager, title: String, position: Int?, valueType: String?) {
        try {
            val listLocation: List<Address>?
            val geocoder = Geocoder(context)
            listLocation = geocoder.getFromLocationName(data.address, 1)
            if (listLocation.isNotEmpty()) {
                val tracking = Tracking()
                tracking.lat = listLocation?.get(0)?.latitude ?: 0.0
                tracking.lng = listLocation?.get(0)?.longitude ?: 0.0
                tracking.title = title
                tracking.id = data.id
                if (data.boothName?.isNotEmpty() == true)
                    tracking.valueName = data.boothName
                else tracking.valueName = data.name

                tracking.valueAddress = data.address

                if (data.phone?.isNotEmpty() == true)
                    tracking.valuePhone = data.phone
                else tracking.valuePhone = data.hotline

                if (data.valueType != null)
                    tracking.valueType = data.valueType
                else
                    tracking.valueType = valueType

                if (position == null)
                    listTracking.add(tracking)
                else {
                    listTracking.removeAt(position)
                    listTracking.add(position, tracking)
                }

                adapter.replaceAll(listTracking)
                mapFragment = childFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment
                mapFragment.getMapAsync(this)

            } else toast("Không tìm thấy vị trí bạn cần tìm")
        } catch (e: IOException) {
            // generic exception handling
            e.printStackTrace()
            toast("Có lỗi xảy ra, vui lòng thử lại")
        } catch (e: InvocationTargetException) {
            // Answer:
            e.cause?.printStackTrace()
            toast("Có lỗi xảy ra, vui lòng thử lại")
        }
    }

    private fun infoWindowAdapter(mMap: GoogleMap, position: Int, item: List<Tracking>) {

        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {

            val layoutInflater = LayoutInflater.from(context)
            @SuppressLint("InflateParams")
            private val mWindow: View = layoutInflater.inflate(R.layout.content_title_map_marker, null)

            override fun getInfoContents(p0: Marker?): View? {
                return null
            }

            @SuppressLint("SetTextI18n")
            override fun getInfoWindow(p0: Marker?): View? {
                mWindow.apply {
                    p0?.let {
                        for (i in 0..position) {
                            if (it.id == "m$i") {
                                tv_map_title.visibility = if (item[i].title?.isNotEmpty() == true) View.VISIBLE else View.GONE
                                tv_map_title.text = "<b>${item[i].title
                                        ?: ""}</b>".asHtml()
                                tv_map_name.text = "Tên: <b>${item[i].valueName
                                        ?: ""}</b>".asHtml()
                                val sdt = "Sđt: <b>${item[i].valuePhone ?: ""}</b>".asHtml()
                                tv_map_phone.setPhone(sdt, item[i].valuePhone ?: "")
                                tv_map_address.text = "Địa chỉ: <b>${item[i].valueAddress}</b>".asHtml()
                            }
                        }
                    }
                }
                return mWindow
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        mMap?.let {
            val uiSettings = it.uiSettings
            uiSettings?.isMyLocationButtonEnabled = true
            uiSettings?.isZoomControlsEnabled = true
            uiSettings?.isZoomGesturesEnabled = true
            uiSettings?.isCompassEnabled = true
            uiSettings?.isRotateGesturesEnabled = true
            uiSettings?.isScrollGesturesEnabled = false
            if (listTracking.isNotEmpty()) {
                val latLng = LatLng(listTracking[0].lat, listTracking[0].lng)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
            }

            val latLngBounds = LatLngBounds.Builder()

            for (i in listTracking.indices) {
//                infoWindowAdapter(it, i, listTracking)
                val latLng = LatLng(listTracking[i].lat, listTracking[i].lng)
                latLngBounds.include(latLng)
                val text = TextView(context)
                text.text = "  " + (i + 1)
                text.setTextColor(Color.WHITE)
                val generator = IconGenerator(context)
                generator.setBackground(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_waypoint) })
                generator.setContentView(text)
                val icon: Bitmap = generator.makeIcon()
                if (i == 0) {
                    if (listTracking.size > 1)
                        countLoop++
                    mMap?.addMarker(latLng.let {
                        MarkerOptions().position(it)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    })
                    FROM = latLng
                }

                if (i > 0 && i != listTracking.size - 1) {
                    mMap?.addMarker(latLng.let {
                        MarkerOptions().position(it)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    })
                    waypoint.add(latLng)
                }

                if (i == listTracking.size - 1) {
                    mMap?.addMarker(latLng.let {
                        MarkerOptions().position(it).title((i + 1).toString())
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    })
                    END = latLng
                }

                if (FROM != LatLng(0.0, 0.0) && END != LatLng(0.0, 0.0)) {
                    waypoint.add(0, FROM)
                    waypoint.add(waypoint.size, END)
                    mMap?.addPolyline(PolylineOptions().addAll(waypoint).width(5.0f).color(Color.RED))

                    mMap?.setOnMapLoadedCallback {
                        val bounds: LatLngBounds = latLngBounds.build()
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    }
                }
            }
        }
    }
}