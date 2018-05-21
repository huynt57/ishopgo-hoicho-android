package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.postmanager.PostProvider


class PostObject : IdentityData(), PostProvider {
    override fun provideTitle(): String {
        return name ?: ""
    }

    override fun provideTime(): String {
        val time = Toolbox.formatApiDateTime(createdAt ?: "")
        return "$time | Đăng bởi $accountName | $statusText"
    }

    override fun provideCategoryName(): String {
        return categoryName ?: ""
    }


    @SerializedName("name")
    @Expose
    private val name: String? = null
    @SerializedName("created_at")
    @Expose
    private val createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    private val updatedAt: String? = null
    @SerializedName("status")
    @Expose
    private val status: Int? = null
    @SerializedName("status_text")
    @Expose
    private val statusText: String? = null
    @SerializedName("account_name")
    @Expose
    private val accountName: String? = null
    @SerializedName("category_id")
    @Expose
    private val categoryId: Int? = null
    @SerializedName("category_name")
    @Expose
    private val categoryName: String? = null
}