package ishopgo.com.exhibition.ui.main.home.expo_fair

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.main.map.ExpoDetailActivity
import kotlinx.android.synthetic.main.fragment_expo_fair_home.*

class ExpoFairHomeFragment : BaseFragment() {

    private var data: ExpoConfig? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expo_fair_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        data = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data != null) {
            val converted = ConverterExpoConfig().convert(data!!)
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

            view_setting.visibility = View.GONE
        }

        cardView_expo_fair.setOnClickListener {
            if (data?.id != -1L) {
                val intent = Intent(requireContext(), ExpoDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(intent)
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
                    return "Thời gian: ${from.startTime?.asDateTime()
                            ?: ""} - ${from.endTime?.asDateTime() ?: ""}"
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

    companion object {

        fun newInstance(params: Bundle): ExpoFairHomeFragment {
            val f = ExpoFairHomeFragment()
            f.arguments = params

            return f
        }
    }

}