package ishopgo.com.exhibition.ui.main.product.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_banner_image.*

class ImagesProductFragment : BaseFragment() {

    private var image: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_banner_image, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        image = arguments?.getString(Const.TransferKey.EXTRA_STRING_LIST) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view.context)
                .load(image)
                .apply(RequestOptions().placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder))
                .into(view_banner_image)
    }

    companion object {

        fun newInstance(params: Bundle): ImagesProductFragment {
            val f = ImagesProductFragment()
            f.arguments = params

            return f
        }
    }

}