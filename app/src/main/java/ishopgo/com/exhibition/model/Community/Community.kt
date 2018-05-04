package ishopgo.com.exhibition.model.Community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.community.CommunityProductProvider
import ishopgo.com.exhibition.ui.community.CommunityProvider


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
        return createdAt
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
    private val createdAt: String = ""
    @SerializedName("shop_id")
    @Expose
    private val shopId: Int = 0
    @SerializedName("content")
    @Expose
    private val content: String = ""
    @SerializedName("account_id")
    @Expose
    private val accountId: Int = 0
    @SerializedName("account_name")
    @Expose
    private val accountName: String = ""
    @SerializedName("account_image")
    @Expose
    private val accountImage: String = ""
    @SerializedName("product")
    @Expose
    private val product: CommunityProduct? = null
    @SerializedName("images")
    @Expose
    private val images: MutableList<String> = mutableListOf()
    @SerializedName("like_count")
    @Expose
    private val likeCount: Int = 0
    @SerializedName("comment_count")
    @Expose
    private val commentCount: Int = 0
    @SerializedName("share_count")
    @Expose
    private val shareCount: Int = 0
}