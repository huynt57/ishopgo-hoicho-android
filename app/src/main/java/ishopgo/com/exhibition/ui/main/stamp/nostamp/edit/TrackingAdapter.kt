package ishopgo.com.exhibition.ui.main.stamp.nostamp.edit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Tracking
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.content_title_map_marker.view.*
import kotlinx.android.synthetic.main.item_tracking.view.*

class TrackingAdapter(private var item: List<Tracking> = mutableListOf(), private var childFragment: FragmentManager? = null) : ClickableAdapter<Tracking>(), OnMapReadyCallback, DirectionCallback {
    companion object {
        const val TRACKING_MAP = 0
        const val TRACKING_LIST = 1
    }
    private var context: Context? = null

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
//    override fun getChildLayoutResource(viewType: Int): Int {
//        return R.layout.item_tracking
//    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == itemCount - 1) R.layout.item_tracking_map else R.layout.item_tracking
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) TRACKING_MAP else TRACKING_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Tracking> {
        return if (viewType == TRACKING_MAP) MapHolder(v) else Holder(v, TrackingConverter())
//        return Holder(v, TrackingConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Tracking>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class MapHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<Tracking>(v) {
        override fun populate(data: Tracking) {
            super.populate(data)
            itemView.apply {
                this@TrackingAdapter.context = context
                if (childFragment != null) {
                    val mapFragment = childFragment!!.findFragmentById((R.id.map)) as SupportMapFragment
                    mapFragment.getMapAsync(this@TrackingAdapter)
                }
            }
        }
    }

    inner class Holder(v: View, private val converter: Converter<Tracking, TrackingProvider>) : BaseRecyclerViewAdapter.ViewHolder<Tracking>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Tracking) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                view_stt.text = "${adapterPosition + 1}"
                view_shop_name.text = convert.provideShop()
                view_shop_address.text = convert.provideAddress()
                view_shop_title.text = convert.provideTitle()
                view_shop_phone.setPhone(convert.providePhone(), data.valueSync?.phone ?: "")

                view_shop_title.visibility = if (convert.provideTitle().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_name.visibility = if (convert.provideShop().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_address.visibility = if (convert.provideAddress().isNotEmpty()) View.VISIBLE else View.GONE
                view_shop_phone.visibility = if (convert.providePhone().isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    interface TrackingProvider {
        fun providePhone(): CharSequence
        fun provideTitle(): CharSequence
        fun provideShop(): CharSequence
        fun provideAddress(): CharSequence
    }

    class TrackingConverter : Converter<Tracking, TrackingProvider> {
        override fun convert(from: Tracking): TrackingProvider {
            return object : TrackingProvider {
                override fun provideTitle(): CharSequence {
                    return from.title ?: ""
                }

                override fun provideShop(): CharSequence {
                    return from.valueSync?.name ?: ""
                }

                override fun providePhone(): CharSequence {
                    return from.valueSync?.phone ?: ""
                }

                override fun provideAddress(): CharSequence {
                    return from.valueSync?.address ?: ""
                }
            }
        }
    }
}