package ishopgo.com.exhibition.ui.imageviewer

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment

class ImageViewerFragmentActionBar : BaseActionBarFragment() {
    private var image_Link: String = ""

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, ImageViewerFragment.newInstance(image_Link, Bundle())).commit()
    }

    companion object {

        fun newInstance(image_Link: String, params: Bundle): ImageViewerFragmentActionBar {
            val fragment = ImageViewerFragmentActionBar()
            fragment.arguments = params
            fragment.image_Link = image_Link

            return fragment
        }
    }
}