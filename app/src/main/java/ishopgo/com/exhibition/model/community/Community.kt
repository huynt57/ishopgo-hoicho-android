package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.community.CommunityProductProvider
import ishopgo.com.exhibition.ui.community.CommunityProvider
import ishopgo.com.exhibition.ui.widget.Toolbox


/**
 * Created by hoangnh on 5/3/2018.
 */
class Community : IdentityData(), CommunityProvider {
    override fun provideLiked(): Int {
        return liked ?: 0
    }

    override fun providerId(): Long {
        return id
    }

    override fun providerUserName(): String {
        return accountName ?: ""
    }

    override fun providerUserAvatar(): String {
        return accountImage ?: ""
    }

    override fun provideContent(): String {
        return content ?: ""
    }

    override fun provideTime(): String {
        return Toolbox.formatApiDateTime(createdAt)
    }

    override fun provideLikeCount(): Int {
        return likeCount ?: 0
    }

    override fun provideCommentCount(): Int {
        return commentCount ?: 0
    }

    override fun provideShareCount(): Int {
        return shareCount ?: 0
    }

    override fun provideProduct(): CommunityProductProvider? {
        return product
    }

    override fun provideListImage(): MutableList<String> {
        return images ?: mutableListOf()
    }

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Long? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_image")
    @Expose
    var accountImage: String? = null
    @SerializedName("product")
    @Expose
    var product: CommunityProduct? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("liked")
    @Expose
    var liked: Int? = null
    @SerializedName("like_count")
    @Expose
    var likeCount: Int? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
    @SerializedName("share_count")
    @Expose
    var shareCount: Int? = null
}