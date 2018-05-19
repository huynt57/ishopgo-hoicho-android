package ishopgo.com.exhibition.ui.chat.local.conversation

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/11/18. HappyCoding!
 *
 * ViewModel to foward data from activity to fragment
 */
class ConversationSharedViewModel : BaseApiViewModel(), AppComponent.Injectable {

    var newMessage = MutableLiveData<PusherChatMessage>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    /**
     * simply forward this message
     */
    fun receiveMessage(it: PusherChatMessage) {
        newMessage.postValue(it)
    }


}