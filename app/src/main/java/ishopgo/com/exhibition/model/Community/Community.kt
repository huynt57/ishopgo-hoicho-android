package ishopgo.com.exhibition.model.Community

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
    override fun providerId(): Long {
        return id
    }

    override fun providerUserName(): String {
        return accountName
    }

    override fun providerUserAvatar(): String {
        return accountImage
    }

    override fun provideContent(): String {
        return content
    }

    override fun provideTime(): String {
        return Toolbox.formatApiDateTime(createdAt)
    }

    override fun provideLikeCount(): Int {
        return likeCount
    }

    override fun provideCommentCount(): Int {
        return commentCount
    }

    override fun provideShareCount(): Int {
        return shareCount
    }

    override fun provideProduct(): CommunityProductProvider? {
        return product
    }

    override fun provideListImage(): MutableList<String> {
        return images
    }

    @SerializedName("created_at")
    @Expose
    var createdAt: String = ""
    @SerializedName("shop_id")
    @Expose
    var shopId: Long = 0
    @SerializedName("content")
    @Expose
    var content: String = ""
    @SerializedName("account_id")
    @Expose
    var accountId: Long = 0
    @SerializedName("account_name")
    @Expose
    var accountName: String = ""
    @SerializedName("account_image")
    @Expose
    var accountImage: String = ""
    @SerializedName("product")
    @Expose
    var product: CommunityProduct? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String> = mutableListOf()
    @SerializedName("like_count")
    @Expose
    var likeCount: Int = 0
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int = 0
    @SerializedName("share_count")
    @Expose
    var shareCount: Int = 0
}