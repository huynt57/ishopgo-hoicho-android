package ishopgo.com.exhibition.ui.chat.local.conversation

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox

/**
 * Created by xuanhong on 4/9/18. HappyCoding!
 */
class ConversationActivity : BaseSingleFragmentActivity() {
    companion object {
        private val TAG = "ConversationActivity"
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return ConversationFragment.newInstance(startupOption)
    }

    private var conversationId: String = ""

    override fun startupOptions(): Bundle {
        val bundle = Bundle()
        if (intent?.hasExtra(Const.TransferKey.EXTRA_JSON) == true) {
            val json = intent.getStringExtra(Const.TransferKey.EXTRA_JSON)
            val localConversationItem = try {
                Toolbox.gson.fromJson(json, LocalConversationItem::class.java)
            } catch (e: Exception) {
                null
            }

            localConversationItem?.let {
                conversationId = it.idConversions ?: ""
            }
            bundle.putString(Const.TransferKey.EXTRA_JSON, json)
        } else
            finish()

        return bundle
    }

}