package ishopgo.com.exhibition.ui.chat.local.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class MemberProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_map_host_fragment).navigateUp()
    }

}