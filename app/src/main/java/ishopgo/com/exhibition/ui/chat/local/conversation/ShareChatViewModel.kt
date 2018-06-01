package ishopgo.com.exhibition.ui.chat.local.conversation

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 5/31/18. HappyCoding!
 */
class ShareChatViewModel: BaseApiViewModel() {

    var newMessage = MutableLiveData<PusherChatMessage>()

    /**
     * simply forward this message
     */
    fun resolveMessage(message: PusherChatMessage): Boolean {
        newMessage.postValue(message)
        return true
    }


}