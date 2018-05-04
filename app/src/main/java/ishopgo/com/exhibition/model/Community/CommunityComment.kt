package ishopgo.com.exhibition.model.Community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.community.CommunityCommentProvider
import ishopgo.com.exhibition.ui.widget.Toolbox


/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityComment : IdentityData(), CommunityCommentProvider {
    override fun providerAccountImage(): String {
        return accountImage
    }

    override fun providerAccountName(): String {
        return accountName
    }

    override fun providerAccountId(): Long {
        return id
    }

    override fun providerPostId(): Long {
        return postId
    }

    override fun providerUpdatedAt(): String {
        return  Toolbox.formatApiDateTime(updatedAt)

    }

    override fun providerCreatedAt(): String {
        return  Toolbox.formatApiDateTime(createdAt)
    }

    override fun providerImages(): MutableList<String> {
        return images
    }

    override fun provideCommentCount(): Long {
        return commentCount
    }

    override fun providerContent(): String {
        return content
    }

    @SerializedName("content")
    @Expose
    var content: String = ""
    @SerializedName("account_name")
    @Expose
    var accountName: String = ""
    @SerializedName("account_image")
    @Expose
    var accountImage: String = ""
    @SerializedName("post_id")
    @Expose
    var postId: Long = 0
    @SerializedName("status")
    @Expose
    var status: Long = 0
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String = ""
    @SerializedName("created_at")
    @Expose
    var createdAt: String = ""
    @SerializedName("images")
    @Expose
    var images = mutableListOf<String>()
    @SerializedName("comment_count")
    @Expose
    var commentCount: Long = 0
//    @SerializedName("last_comment")
//    @Expose
//    var lastComment: Long = 0
}