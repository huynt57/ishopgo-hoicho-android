package ishopgo.com.exhibition.ui.main.map

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ExpoDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ExpoDetailFragment2.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return Navigation.findNavController(this, R.id.nav_map_host_fragment).navigateUp()
//    }
}
