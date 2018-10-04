package ishopgo.com.exhibition.ui.banner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Banner
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import kotlinx.android.synthetic.main.fragment_banner_image.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class BannerImageFragment : BaseFragment() {

    private var banner: Banner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_banner_image, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        banner = Toolbox.gson.fromJson(json, Banner::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view.context)
                .load(banner?.image)
                .apply(RequestOptions()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_banner_image)

        view_banner_image.setOnClickListener {
            val intent = Intent(it.context, FullDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_URL, banner?.url ?: "")
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(params: Bundle): BannerImageFragment {
            val f = BannerImageFragment()
            f.arguments = params

            return f
        }
    }

}