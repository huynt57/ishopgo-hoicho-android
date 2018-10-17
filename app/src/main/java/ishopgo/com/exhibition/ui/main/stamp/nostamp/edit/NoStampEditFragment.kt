package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_no_stamp_edit.*
import kotlinx.android.synthetic.main.content_title_map_marker.view.*

class NoStampEditFragment : BaseFragment(), OnMapReadyCallback, DirectionCallback {
    private var adapter = TrackingAdapter()
    private lateinit var item: List<Tracking>
    private var stampId = 0L
    private lateinit var viewModel: NoStampViewModel

    companion object {

        fun newInstance(params: Bundle): NoStampEditFragment {
            val fragment = NoStampEditFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stampId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_no_stamp_edit, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_edit_no_stamp.setOnClickListener {
            //                viewModel.editNoStampDetail(stampId, edit_no_stamp_name.text.toString(), edit_no_stamp_count.text.toString())
        }
        rv_tracking.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_tracking.layoutManager = layoutManager
        layoutManager.isAutoMeasureEnabled = true
        rv_tracking.isNestedScrollingEnabled = false
        rv_tracking.setHasFixedSize(false)
        rv_tracking.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
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
                edit_no_stamp_name.setText(it.code ?: "")
                edit_no_stamp_count.setText("${it.limitedAccess ?: 0}")
                item = it.trackings ?: mutableListOf()
                adapter.replaceAll(item)

            }
        })

        viewModel.editNoStampSusscess.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        })

        viewModel.getDetailNoStamp(stampId)
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        mMap?.let {
            val uiSettings = it.uiSettings
            uiSettings?.isMyLocationButtonEnabled = true
            uiSettings?.isZoomControlsEnabled = true
            uiSettings?.isZoomGesturesEnabled = true
            uiSettings?.isCompassEnabled = true
            uiSettings?.isRotateGesturesEnabled = true
            if (item.isNotEmpty()) {
                val latLng = LatLng(item[0].lat, item[0].lng)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
            }

//        mMap?.setOnMapClickListener(this)

//        mMap?.let { infoWindowAdapter(it) }
            GoogleDirectionConfiguration.getInstance().isLogEnabled = true
            for (i in item.indices) {
                infoWindowAdapter(it, i, item)
                val latLng = LatLng(item[i].lat, item[i].lng)
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

                if (FROM != LatLng(0.0, 0.0) && END != LatLng(0.0, 0.0))
                    GoogleDirection.withServerKey(context?.getString(R.string.app_map_key))
                            .from(FROM)
                            .and(waypoint)
                            .to(END)
                            .transportMode(TransportMode.DRIVING)
                            .execute(this)
            }
        }
    }

    override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
        direction?.let {
            if (direction.isOK) {
                val route = direction.routeList[0]
                val legCount = route.legList.size
                for (index in 0 until legCount) {
                    val leg = route.legList[index]

                    val stepList = leg.stepList
                    val polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, 5, Color.RED, 3, Color.BLUE)
                    for (polylineOption in polylineOptionList) {
                        mMap?.addPolyline(polylineOption)
                    }
                }
                setCameraWithCoordinationBounds(route)
            }
        }
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    override fun onDirectionFailure(t: Throwable?) {
        Log.d("MapException", t?.message.toString())
    }

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
}