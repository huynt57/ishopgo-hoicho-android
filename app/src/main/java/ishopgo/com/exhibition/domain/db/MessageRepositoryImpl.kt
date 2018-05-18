package ishopgo.com.exhibition.domain.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ishopgo.com.exhibition.model.chat.PreparedMessage

/**
 * Created by xuanhong on 5/18/18. HappyCoding!
 */
class MessageRepositoryImpl(private val database: AppDB) : MessageRepository {
    override fun setMessageFailed(id: String): Completable {
        return Completable.fromAction { database.preparedMessageDao().setMessageFailed(id) }
    }

    override fun getFailedMessages(idConversation: String): Single<List<PreparedMessage>> {
        return database.preparedMessageDao().getFailedMessages(idConversation)
    }

    override fun add(message: PreparedMessage): Completable {
        return Completable.fromAction { database.preparedMessageDao().add(message) }
    }

    override fun update(message: PreparedMessage): Completable {
        return Completable.fromAction { database.preparedMessageDao().update(message) }
    }

    override fun getById(id: String): Flowable<PreparedMessage> {
        return database.preparedMessageDao().getById(id)
    }

    override fun delete(message: PreparedMessage): Completable {
        return Completable.fromAction { database.preparedMessageDao().delete(message) }
    }

    override fun delete(id: String): Completable {
        return Completable.fromAction { database.preparedMessageDao().delete(id) }
    }
}