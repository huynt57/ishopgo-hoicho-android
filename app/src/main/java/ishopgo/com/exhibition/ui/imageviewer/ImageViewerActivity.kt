package ishopgo.com.exhibition.ui.imageviewer

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ImageViewerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ImageViewerFragmentActionBar.newInstance(intent.getStringExtra("ImageLink"), startupOption)
    }
}