package ishopgo.com.exhibition.ui.main.product.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.reflect.TypeToken
import com.google.maps.android.ui.IconGenerator
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.content_title_map_marker.view.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductStampMapFragment : BaseActionBarFragment(), OnMapReadyCallback {
    private lateinit var item: List<ProductDetail.StampTracking>
    private var mMap: GoogleMap? = null
    private val ZOOM_LEVEL = 15f
    private var FROM = LatLng(0.0, 0.0)
    private var waypoint: ArrayList<LatLng> = ArrayList()
    private var END = LatLng(0.0, 0.0)

    private fun infoWindowAdapter(mMap: GoogleMap, position: Int, item: List<ProductDetail.StampTracking>) {

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
                                tv_map_address.text = "Địa chỉ: <b>${item[i].valueAddress
                                        ?: ""}</b>".asHtml()
                            }
                        }
                    }
                }
                return mWindow
            }
        })

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
//                    mMap?.addPolyline(PolylineOptions().addAll(waypoint).width(5.0f).color(Color.RED))

                    mMap?.setOnMapLoadedCallback{
                        val bounds: LatLngBounds = latLngBounds.build()
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    }

                }
            }
        }
    }

    companion object {

        fun newInstance(params: Bundle): ProductStampMapFragment {
            val fragment = ProductStampMapFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_stamp_map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        if (item.isNotEmpty()) {
            val mapFragment = childFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = Toolbox.gson.fromJson<MutableList<ProductDetail.StampTracking>>(arguments?.getString(Const.TransferKey.EXTRA_JSON),
                object : TypeToken<MutableList<ProductDetail.StampTracking>>() {}.type)
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Bản đồ điểm bán sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}