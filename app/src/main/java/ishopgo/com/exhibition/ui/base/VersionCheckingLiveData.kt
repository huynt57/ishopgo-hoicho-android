package ishopgo.com.exhibition.ui.base

import android.arch.lifecycle.LiveData
import com.google.firebase.database.*

/**
 * Created by xuanhong on 5/8/18. HappyCoding!
 */
class VersionCheckingLiveData(ref: DatabaseReference) : LiveData<DataSnapshot>() {

    var query: Query = ref

    private var eventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError?) {

        }

        override fun onDataChange(p0: DataSnapshot?) {
            p0?.let { value = it }
        }

    }

    override fun onActive() {
        super.onActive()
        query.addListenerForSingleValueEvent(eventListener)
    }

    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(eventListener)
    }

}