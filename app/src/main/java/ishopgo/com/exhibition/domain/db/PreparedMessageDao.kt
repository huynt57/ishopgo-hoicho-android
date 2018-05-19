package ishopgo.com.exhibition.domain.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.model.chat.PreparedMessage
import ishopgo.com.exhibition.model.chat.PreparedMessage.Companion.STATUS
import ishopgo.com.exhibition.model.chat.PreparedMessage.Companion.TABLE_NAME
import ishopgo.com.exhibition.model.chat.PreparedMessage.Companion.TEMP_ID

/**
 * Created by xuanhong on 5/18/18. HappyCoding!
 */
@Dao
interface PreparedMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(message: PreparedMessage)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(message: PreparedMessage)

    @Query("SELECT * FROM $TABLE_NAME WHERE $TEMP_ID = :id LIMIT 1")
    fun getById(id: String): Flowable<PreparedMessage>

    @Query("SELECT * FROM $TABLE_NAME WHERE $TEMP_ID = :idConversation")
    fun getFailedMessages(idConversation: String): Single<List<PreparedMessage>>

    @Query("UPDATE $TABLE_NAME SET $STATUS = ${IChatMessage.STATUS_FAILED} WHERE $TEMP_ID = :tempId")
    fun setMessageFailed(tempId: String)

    @Delete
    fun delete(message: PreparedMessage)

    @Query("DELETE FROM $TABLE_NAME WHERE $TEMP_ID = :tempId")
    fun delete(tempId: String)

}
