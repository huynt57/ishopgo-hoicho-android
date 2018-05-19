package ishopgo.com.exhibition.domain.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ishopgo.com.exhibition.model.chat.PreparedMessage

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
interface MessageRepository {

    fun add(message: PreparedMessage): Completable

    fun update(message: PreparedMessage): Completable

    fun getById(id: String): Flowable<PreparedMessage>

    fun getFailedMessages(idConversation: String): Single<List<PreparedMessage>>

    fun setMessageFailed(id: String): Completable

    fun delete(message: PreparedMessage): Completable

    fun delete(id: String): Completable
}