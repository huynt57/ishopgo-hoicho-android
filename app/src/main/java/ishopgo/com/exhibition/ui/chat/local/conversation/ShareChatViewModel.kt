package ishopgo.com.exhibition.ui.chat.local.conversation

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 5/31/18. HappyCoding!
 */
class ShareChatViewModel : BaseApiViewModel() {

    companion object {
        const val STATE_CONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_DISCONNECTED = 2
    }

    var newMessage = MutableLiveData<PusherChatMessage>()
    var connectionState = MutableLiveData<Int>()

    /**
     * simply forward this message
     */
    fun resolveMessage(message: PusherChatMessage) {
        newMessage.postValue(message)
    }

    fun updateConnectionState(state: Int) {
        connectionState.postValue(state)
    }


}