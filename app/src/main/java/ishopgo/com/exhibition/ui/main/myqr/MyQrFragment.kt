package ishopgo.com.exhibition.ui.main.myqr

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_myqr.*

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrFragment : BaseFragment() {

    private lateinit var viewModel: MyQrViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myqr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MyQrViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.qrImage.observe(this, Observer { url ->
            url?.let {
                //                view_qrcode.setImageBitmap(QRCode.from("http://hoicho.ishopgo.com/gian-hang/gian-hang-17365?booth=17365").withSize(300, 300).bitmap())
                Glide.with(view_qrcode.context)
                        .load(it)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_qrcode)
            }
        })

        viewModel.loadQrCode()
    }
}