package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_no_stamp_edit.*
import java.io.IOException
import java.lang.reflect.InvocationTargetException

class NoStampEditFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, DirectionCallback {
    override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
//        if (direction?.isOK) {
//            val route = direction?.routeList[0]
//            val legCount = route.legList.size
//            for (index in 0 until legCount) {
//                val leg = route.legList[index]
//
//                val stepList = leg.stepList
//                val polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, 5, Color.RED, 3, Color.BLUE)
//                for (polylineOption in polylineOptionList) {
//                    mMap?.addPolyline(polylineOption)
//                }
//            }
//            setCameraWithCoordinationBounds(route)
//        }
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    override fun onDirectionFailure(t: Throwable?) {
        toast(t?.message.toString())
    }

    private var mMap: GoogleMap? = null
    private val ZOOM_LEVEL = 15f
    private var lat: String = ""
    private var lng: String = ""
    private var adapter = TrackingAdapter()

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        val uiSettings = mMap?.uiSettings
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isZoomControlsEnabled = true
        uiSettings?.isZoomGesturesEnabled = true
        uiSettings?.isCompassEnabled = true
        uiSettings?.isRotateGesturesEnabled = true
//        item.address?.let { mMap?.let { it1 -> infoWindowAdapter(it1, it) } }
//        val latLng = item.lat?.let { item.lng?.let { it1 -> LatLng(it, it1) } }

//        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
//        mMap?.addMarker(latLng?.let { MarkerOptions().position(it) })?.showInfoWindow()

        mMap?.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng?) {
        try {
            if (p0 != null) {
                mMap?.clear()
                val listLocation: List<Address>?
                val geocoder = Geocoder(context)
                listLocation = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                val address: Address? = listLocation?.get(0)
//                address?.getAddressLine(0)?.let { mMap?.let { it1 -> infoWindowAdapter(it1, it) } }
                lat = address?.latitude.toString()
                lng = address?.longitude.toString()
//                addressName = address?.getAddressLine(0).toString()
//                edt_db_location.setText(address?.getAddressLine(0))
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(p0, ZOOM_LEVEL))
                mMap?.addMarker(p0.let { MarkerOptions().position(it) })?.showInfoWindow()
            } else toast("Không tìm thấy địa điểm này")
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

        rv_tracking.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_tracking.adapter = adapter
        rv_tracking.isNestedScrollingEnabled = false
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
            p?.let {
                edit_no_stamp_name.setText(it.code ?: "")
                edit_no_stamp_count.setText("${it.limitedAccess ?: 0}")
                adapter.replaceAll(it.trackings ?: mutableListOf())
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
}