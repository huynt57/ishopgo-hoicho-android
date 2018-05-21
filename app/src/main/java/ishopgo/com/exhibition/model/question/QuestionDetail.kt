package ishopgo.com.exhibition.model.question

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.questmanager.detail.QuestDetailProvider


class QuestionDetail : QuestDetailProvider {
    override fun provideTitle(): String {
        return "Tiêu đề: $title"
    }

    override fun provideTime(): String {
        return Toolbox.formatApiDateTime(createdAt ?: "")
    }

    override fun provideAnswer(): String {
        return "Câu trả lời: $answer"
    }


    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("answer")
    @Expose
    var answer: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Long = -1L
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
}