package ishopgo.com.exhibition.ui.chat.local.service.utils

/**
 * Created by xuanhong on 4/10/18. HappyCoding!
 */
object PusherUtils {

    fun isPublicChannel(channelName: String) = !isPrivateChannel(channelName) && !isPresenceChannel(channelName)

    fun isPrivateChannel(channelName: String) = channelName.startsWith("private-")

    fun isPresenceChannel(channelName: String) = channelName.startsWith("presence-")
}