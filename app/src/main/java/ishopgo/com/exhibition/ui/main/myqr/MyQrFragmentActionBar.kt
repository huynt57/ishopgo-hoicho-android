package ishopgo.com.exhibition.ui.main.myqr

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrFragmentActionBar : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    companion object {

        fun newInstance(params: Bundle): MyQrFragmentActionBar {
            val fragment = MyQrFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, MyQrFragment(), "MyQrFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Mã QR gian hàng")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }

        toolbar.rightButton(R.drawable.ic_file_download_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag("MyQrFragment") as? MyQrFragment
            fragment?.let {
                fragment.download()
            }
        }
    }
}