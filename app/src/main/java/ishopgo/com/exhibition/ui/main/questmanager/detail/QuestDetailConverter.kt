package ishopgo.com.exhibition.ui.main.questmanager.detail

import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime

/**
 * Created by xuanhong on 6/15/18. HappyCoding!
 */
class QuestDetailConverter : Converter<QuestionDetail, QuestDetailProvider> {

    override fun convert(from: QuestionDetail): QuestDetailProvider {
        return object : QuestDetailProvider {
            override fun provideTitle(): String {
                return from.title ?: ""
            }

            override fun provideTime(): String {
                return from.createdAt?.asDateTime() ?: ""
            }

            override fun provideAnswer(): String {
                return "Câu trả lời: ${from.answer}"
            }


        }
    }


}