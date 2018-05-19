package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.content_chat_stream_action_reply.view.*

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class ChatReplyActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    var listener: ChatReplyListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.content_chat_stream_action_reply, this)
        btn_send.setOnClickListener {
            val text = edt_comment.text.toString()
            if (text.isNotBlank())
                listener?.sendTextMessage(this, text.trim())

            edt_comment.setText("")
        }

        attach_extra.setOnClickListener {
            listener?.addAttachments(this)
        }

    }

    fun setText(text: CharSequence) {
        edt_comment.setText(text)
    }

    fun clearText() {
        edt_comment.text = null
    }

    interface ChatReplyListener {
        fun addAttachments(actionView: ChatReplyActionView)
        fun sendTextMessage(actionView: ChatReplyActionView, text: String)
    }

}