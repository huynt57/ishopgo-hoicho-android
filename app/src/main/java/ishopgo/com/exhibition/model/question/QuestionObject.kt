package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.questmanager.QuestProvider


class QuestionObject : IdentityData(), QuestProvider {
    override fun provideTitle(): String {
        return name ?: ""
    }

    override fun provideTime(): String {
        val time = Toolbox.formatApiDateTime(createdAt ?: "")
        return "$time | Đăng bởi $accountName"
    }

    override fun provideCategoryName(): String {
        return categoryName ?: ""
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("status")
    @Expose
    var status: Int = 0
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Long = -1L
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
}