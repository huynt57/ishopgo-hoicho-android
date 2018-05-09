package ishopgo.com.exhibition.ui.imageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment

class ImageViewerFragment : BaseFragment() {
    private var image_Link: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_viewer_only, container, false)
    }

    companion object {
        fun newInstance(image_Link: String, params: Bundle): ImageViewerFragment {
            val fragment = ImageViewerFragment()
            fragment.image_Link = image_Link
            fragment.arguments = params
            return fragment
        }
    }
}