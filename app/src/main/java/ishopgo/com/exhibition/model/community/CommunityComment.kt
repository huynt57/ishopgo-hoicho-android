package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.community.CommunityCommentProvider
import ishopgo.com.exhibition.ui.extensions.asDateTime


/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityComment : IdentityData(), CommunityCommentProvider {
    override fun providerAccountImage(): String {
        return accountImage ?: ""
    }

    override fun providerAccountName(): String {
        return accountName ?: ""
    }

    override fun providerAccountId(): Long {
        return id
    }

    override fun providerPostId(): Long {
        return postId ?: 0
    }

    override fun providerUpdatedAt(): String {
        return updatedAt?.asDateTime() ?: ""

    }

    override fun providerCreatedAt(): String {
        return createdAt?.asDateTime() ?: ""
    }

    override fun providerImages(): MutableList<String> {
        return images ?: mutableListOf()
    }

    override fun provideCommentCount(): Int {
        return commentCount ?: 0
    }

    override fun providerContent(): String {
        return content ?: ""
    }

    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_image")
    @Expose
    var accountImage: String? = null
    @SerializedName("post_id")
    @Expose
    var postId: Long? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
//    @SerializedName("last_comment")
//    @Expose
//    var lastComment: Long = 0
}