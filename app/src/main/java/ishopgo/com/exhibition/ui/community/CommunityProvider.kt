package ishopgo.com.exhibition.ui.community

/**
 * Created by hoangnh on 4/23/2018.
 */
interface CommunityProvider {
    fun userName(): String
    fun userAvatar(): String
    fun currentUserAvatar(): String
    fun communityContent(): String
    fun communityTime(): String
    fun communityLike(): Int
    fun communityComment(): Int
    fun communityShare(): Int
    fun communityProductName(): String
    fun communityProductCode(): String
    fun communityProductPrice(): String
    fun communityProductImage(): String
    fun communityProductListImage(): MutableList<String>?
}