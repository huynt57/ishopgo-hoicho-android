package ishopgo.com.exhibition.ui.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

/**
 * Created by xuanhong on 5/8/18. HappyCoding!
 */
class VersionCheckingViewModel : ViewModel() {

    fun versionChecking(ref: DatabaseReference): LiveData<DataSnapshot> {
        return VersionCheckingLiveData(ref)
    }
}