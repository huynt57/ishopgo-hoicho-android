package ishopgo.com.exhibition.ui.main.map.config

import android.os.Bundle
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActivity

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoMapConfigActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expo_config_map)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_map_host_fragment).navigateUp()
    }
}