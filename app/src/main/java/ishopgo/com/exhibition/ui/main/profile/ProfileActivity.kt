package ishopgo.com.exhibition.ui.main.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
class ProfileActivity : BaseSingleFragmentActivity() {

    companion object {
        private val TAG = "ProfileActivity"
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProfileFragmentActionBar.newInstance(startupOption)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "token: ${FirebaseInstanceId.getInstance().token}")
    }


}