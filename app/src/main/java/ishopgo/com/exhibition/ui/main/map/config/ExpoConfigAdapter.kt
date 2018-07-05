package ishopgo.com.exhibition.ui.main.map.config

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_expo_config.view.*

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoConfigAdapter : ClickableAdapter<ExpoConfig>() {

     companion object {
        const val CLICK_SETTING = 1
        const val CLICK_DETAIL = 2
    }

    private val canConfigExpo = UserDataManager.currentType == "Chủ hội chợ"

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_expo_config
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ExpoConfig> {
        return ExpoShopHolder(v, ConverterExpoConfig())
    }

    override fun onBindViewHolder(holder: ViewHolder<ExpoConfig>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition), CLICK_DETAIL)
        }

        holder.itemView.view_setting.setOnClickListener {
            val adapter = holder.adapterPosition
            listener?.click(adapter, getItem(adapter), CLICK_SETTING)
        }
    }

    inner class ExpoShopHolder(v: View, private val converter: ConverterExpoConfig) : BaseRecyclerViewAdapter.ViewHolder<ExpoConfig>(v) {

        override fun populate(data: ExpoConfig) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(converted.avatar())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(view_avatar)

                Glide.with(context)
                        .load(converted.qrcode())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(view_qrcode)

                view_name.text = converted.name()
                view_time.text = converted.time()
                view_address.text = converted.address()

                if (canConfigExpo) {
                    view_setting.visibility = View.VISIBLE
                }
                else
                    view_setting.visibility = View.GONE

                if (UserDataManager.currentType == "Quản trị viên") {
                    val listPermission = Toolbox.gson.fromJson<ArrayList<String>>(UserDataManager.listPermission, object : TypeToken<ArrayList<String>>() {}.type)

                    if (listPermission.isNotEmpty())
                        for (i in listPermission.indices) {
                            if (Const.Permission.EXPO_FAIR_EDIT == listPermission[i] || Const.Permission.EXPO_FAIR_SETUP == listPermission[i] || Const.Permission.EXPO_FAIR_DELETE == listPermission[i]) {
                                view_setting.visibility = View.VISIBLE
                                break
                            } else view_setting.visibility = View.GONE
                        }
                }
            }
        }
    }

    class ConverterExpoConfig : Converter<ExpoConfig, ExpoMapConfigProvider> {

        override fun convert(from: ExpoConfig): ExpoMapConfigProvider {
            return object : ExpoMapConfigProvider {
                override fun qrcode(): CharSequence {
                    return from.qrcodePNG ?: ""
                }

                override fun avatar(): CharSequence {
                    return from.image ?: ""
                }

                override fun name(): CharSequence {
                    return from.name ?: ""
                }

                override fun time(): CharSequence {
                    return "Thời gian: ${from.startTime?.asDateTime() ?: ""} - ${from.endTime?.asDateTime() ?: ""}"
                }

                override fun address(): CharSequence {
                    return "Địa điểm: ${from.address ?: ""}"
                }

            }
        }

    }

    interface ExpoMapConfigProvider {

        fun avatar(): CharSequence
        fun qrcode(): CharSequence
        fun name(): CharSequence
        fun time(): CharSequence
        fun address(): CharSequence

    }
}