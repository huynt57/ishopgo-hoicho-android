package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampNoDetail
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.productmanager.add.BoothAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.main.stamp.nostamp.add.TitleAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.edit.map.NoStampMapActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_no_stamp_edit.*
import kotlinx.android.synthetic.main.content_title_map_marker.view.*

class NoStampEditFragment : BaseFragment(), OnMapReadyCallback {
    private var adapter = TrackingAdapter()
    private lateinit var item: List<Tracking>
    private var stampId = 0L
    private lateinit var viewModel: NoStampViewModel
    private var data: StampNoDetail? = null
    private var reloadProvider = false
    private val adapterBooth = BoothAdapter()
    private val adapterBoothCurrent = BoothAdapter()
    private val adapterTitle = TitleAdapter()

    companion object {

        fun newInstance(params: Bundle): NoStampEditFragment {
            val fragment = NoStampEditFragment()
            fragment.arguments = params

            return fragment
        }

        const val ADD_TRACKING = true
    }

    val title = mutableListOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stampId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_edit, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterTitle.replaceAll(title)

        view_show_map.setOnClickListener {
            if (data != null) {
                val intent = Intent(context, NoStampMapActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data!!.trackings))
                startActivity(intent)
            }
        }

        img_add_maker.setOnClickListener {
            showDialogAddMaker(ADD_TRACKING, "")
        }

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
                    showDialogClickTrackingAdapter(data)
                }
            }
        }

        btn_assign_no_stamp.setOnClickListener {
            viewModel.updateStampDiaryInfo(stampId, edit_no_stamp_ngaySX.text.toString(),
                    edit_no_stamp_HSD.text.toString(), edit_no_stamp_soLuong.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(NoStampViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getDataNoStampDetail.observe(this, Observer { p ->
            p?.let { it ->
                data = it
                edit_no_stamp_name.setText(it.code ?: "")
                edit_no_stamp_count.setText("${it.limitedAccess ?: 0}")
                item = it.trackings ?: mutableListOf()
                adapter.replaceAll(item)
                val mapFragment = childFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        })

        viewModel.editNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.dataBooth.observe(this, Observer { p ->
            p?.let {
                if (reloadProvider) adapterBooth.replaceAll(it) else adapterBooth.addAll(it)
            }
        })

        viewModel.shopRelates.observe(this, Observer { p ->
            p?.let {
                adapterBoothCurrent.replaceAll(it)
            }
        })

        viewModel.deleteTrackingSuccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
                toast("Xoá thành công")
            }
        })

        viewModel.createTrackingSucccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
                hideProgressDialog()
                toast("Thêm hành trình thành công")
            }
        })

        viewModel.editTrackingSucccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
                hideProgressDialog()
                toast("Sửa hành trình thành công")
            }
        })

        viewModel.updateStampDiarySucccess.observe(this, Observer { p ->
            p?.let {
                viewModel.getDetailNoStamp(stampId)
                hideProgressDialog()
                toast("Cập nhật thành công")
            }
        })

        reloadProvider = true

        viewModel.getDetailNoStamp(stampId)
        firstLoadProvider()
        if (UserDataManager.currentBoothId > 0)
            viewModel.loadShopRelates(UserDataManager.currentBoothId)
        else viewModel.loadShopRelates(UserDataManager.currentUserId)
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
            if (item.isNotEmpty()) {
                val latLng = LatLng(item[0].lat, item[0].lng)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
            }

//        mMap?.setOnMapClickListener(this)

//        mMap?.let { infoWindowAdapter(it) }
//            GoogleDirectionConfiguration.getInstance().isLogEnabled = true
            val latLngBounds = LatLngBounds.Builder()
            for (i in item.indices) {
                infoWindowAdapter(it, i, item)
                val latLng = LatLng(item[i].lat, item[i].lng)
                latLngBounds.include(latLng)
                val text = TextView(context)
                text.text = "  " + (i + 1)
                text.setTextColor(Color.WHITE)
                val generator = IconGenerator(context)
                generator.setBackground(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_waypoint) })
                generator.setContentView(text)
                val icon: Bitmap = generator.makeIcon()
                if (i == 0) {
                    mMap?.addMarker(latLng.let {
                        MarkerOptions().position(it)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    })
                    FROM = latLng
                }

                if (i > 0 && i != item.size - 1) {
                    mMap?.addMarker(latLng.let {
                        MarkerOptions().position(it)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    })
                    waypoint.add(latLng)
                }

                if (i == item.size - 1) {
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
//                    GoogleDirection.withServerKey(context?.getString(R.string.app_map_key))
//                            .from(FROM)
//                            .and(waypoint)
//                            .to(END)
//                            .transportMode(TransportMode.DRIVING)
//                            .execute(this)
            }
        }
    }

//    override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
//        direction?.let {
//            if (direction.isOK) {
//                val route = direction.routeList[0]
//                val legCount = route.legList.size
//                for (index in 0 until legCount) {
//                    val leg = route.legList[index]
//
//                    val stepList = leg.stepList
//                    val polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, 5, Color.RED, 3, Color.BLUE)
//                    for (polylineOption in polylineOptionList) {
//                        mMap?.addPolyline(polylineOption)
//                    }
//                }
//                setCameraWithCoordinationBounds(route)
//            }
//        }
//    }

//    private fun setCameraWithCoordinationBounds(route: Route) {
//        val southwest = route.bound.southwestCoordination.coordination
//        val northeast = route.bound.northeastCoordination.coordination
//        val bounds = LatLngBounds(southwest, northeast)
//        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
//    }

//    override fun onDirectionFailure(t: Throwable?) {
//        Log.d("MapException", t?.message.toString())
//    }

    private var mMap: GoogleMap? = null
    private val ZOOM_LEVEL = 15f
    private var FROM = LatLng(0.0, 0.0)
    private var waypoint: ArrayList<LatLng> = ArrayList()
    private var END = LatLng(0.0, 0.0)
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
                                tv_map_name.text = "Tên: <b>${item[i].valueSync?.name
                                        ?: ""}</b>".asHtml()
                                val sdt = "Sđt: <b>${item[i].valueSync?.phone ?: ""}</b>".asHtml()
                                tv_map_phone.setPhone(sdt, item[i].valueSync?.phone ?: "")
                                tv_map_address.text = "Địa chỉ: <b>${item[i].valueSync?.address}</b>".asHtml()
                            }
                        }
                    }
                }
                return mWindow
            }
        })
    }

    private fun showDialogAddMaker(type: Boolean, title: String) {
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
                getBoothCurrent(type, title)
                dialog.dismiss()
            }
            val tvSystem = dialog.findViewById(R.id.tv_system) as VectorSupportTextView
            tvSystem.setOnClickListener {
                getBoothSystem(type, title)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun showDialogClickTrackingAdapter(data: Tracking) {
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
                showDialogAddMaker(!ADD_TRACKING, data.title ?: "")
                dialog.dismiss()
            }
            val tvDeleteTracking = dialog.findViewById(R.id.tv_deleteTracking) as VectorSupportTextView
            tvDeleteTracking.setOnClickListener {
                dialogDeleteTracking(data)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun dialogDeleteTracking(data: Tracking) {
        MaterialDialog.Builder(requireContext())
                .content("Bạn có muốn xoá hành trình này không?")
                .positiveText("Có")
                .onPositive { dialog, _ ->
                    viewModel.deleteTracking(data.id)
                    showProgressDialog()
                    dialog.dismiss()
                }
                .negativeText("Không")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun getBoothSystem(type: Boolean, title: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            val edt_search = dialog.findViewById(R.id.edt_search) as TextInputEditText
            textInputLayout.visibility = View.VISIBLE
            textInputLayout.hint = "Tiêu đề danh mục"
            edt_search.isFocusable = false
            edt_search.isFocusableInTouchMode = false
            edt_search.setOnClickListener { getTitle(edt_search) }
            edt_search.setText(if (type == ADD_TRACKING) "Điểm bán lẻ" else title)

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBooth
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    loadMoreProvider(totalItemsCount)
                }
            })
            adapterBooth.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        if (edt_search.text.isEmpty()) {
                            toast("Vui lòng nhập tiêu đề danh mục")
                            edt_search.error = "Tiêu đề danh mục còn trống"
                            return
                        }
                        if (type == ADD_TRACKING)
                            viewModel.createTracking(stampId, edt_search.text.trim().toString(), data.id)
                        else viewModel.editTracking(stampId, edt_search.text.trim().toString(), data.id)
                        showProgressDialog()
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getBoothCurrent(type: Boolean, title: String) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn gian hàng")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val textInputLayout = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            val edt_search = dialog.findViewById(R.id.edt_search) as TextInputEditText
            textInputLayout.visibility = View.VISIBLE
            textInputLayout.hint = "Tiêu đề danh mục"
            edt_search.isFocusable = false
            edt_search.isFocusableInTouchMode = false
            edt_search.setText("Điểm bán lẻ")
            edt_search.setOnClickListener { getTitle(edt_search) }
            edt_search.setText(if (type == ADD_TRACKING) "Điểm bán lẻ" else title)

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager

            rv_search.adapter = adapterBoothCurrent

            adapterBoothCurrent.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
                override fun click(position: Int, data: BoothManager, code: Int) {
                    context?.let {
                        if (edt_search.text.isEmpty()) {
                            toast("Vui lòng nhập tiêu đề danh mục")
                            edt_search.error = "Tiêu đề danh mục còn trống"
                            return
                        }
                        if (type == ADD_TRACKING)
                            viewModel.createTracking(stampId, edt_search.text.trim().toString(), data.id)
                        else viewModel.editTracking(stampId, edt_search.text.trim().toString(), data.id)

                        showProgressDialog()
                        dialog.dismiss()
                    }
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
                    context?.let {
                        dialog.dismiss()
                        view.text = data
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }
}