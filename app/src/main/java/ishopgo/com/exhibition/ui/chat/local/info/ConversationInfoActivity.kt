package ishopgo.com.exhibition.ui.chat.local.info

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.chat.local.info.multi.MultiMemberInfoFragment
import ishopgo.com.exhibition.ui.chat.local.info.single.SingleMemberInfoFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox

/**
 * Created by xuanhong on 4/7/18. HappyCoding!
 */
class ConversationInfoActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        val json = startupOption.getString(Const.TransferKey.EXTRA_JSON)
        val info = try {
            Toolbox.gson.fromJson(json, ConversationInfo::class.java)
        } catch (e: Exception) {
            throw RuntimeException("sai dinh dang")
        }

        val conversationId = startupOption.getString(Const.TransferKey.EXTRA_CONVERSATION_ID)
        info.conversationId = conversationId
        if (info.type == ConversationInfo.TYPE_SINGLE)
            return SingleMemberInfoFragment.newInstance(info)
        else
            return MultiMemberInfoFragment.newInstance(info)
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

}