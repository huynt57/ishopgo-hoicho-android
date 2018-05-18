package ishopgo.com.exhibition.model.chat

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by xuanhong on 12/31/17. HappyCoding!
 *
 * save sending and failed message
 */
@Entity(tableName = PreparedMessage.TABLE_NAME, primaryKeys = arrayOf(PreparedMessage.TEMP_ID))
class PreparedMessage {
    /**
     * temporary id for message
     */
    @ColumnInfo(name = TEMP_ID)
    var idTemp: String = "- ${System.currentTimeMillis()}"

    @ColumnInfo(name = ID_CONVERSATION)
    var idConversation: String? = null

    @ColumnInfo(name = TEXT_CONTENT)
    var text: String? = null

    @ColumnInfo(name = IMAGE_URIS)
    var imageUris: String? = null

    @ColumnInfo(name = FROM)
    var from: Long? = null

    @ColumnInfo(name = STATUS)
    var status: Int = IChatMessage.STATUS_SENDING

    override fun toString(): String {
        return "tempId = [$idTemp], idConv = [$idConversation], text = [$text], imageUris = [$imageUris], from = [$from], status = [$status]"
    }

    companion object {
        const val TABLE_NAME = "prepared_messages"

        const val TEMP_ID = "temp_id"
        const val ID_CONVERSATION = "id_conversion"
        const val TEXT_CONTENT = "text"
        const val IMAGE_URIS = "image_uris"
        const val FROM = "from"
        const val STATUS = "status"
    }
}