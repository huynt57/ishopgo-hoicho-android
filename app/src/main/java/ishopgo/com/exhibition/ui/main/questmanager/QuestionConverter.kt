package ishopgo.com.exhibition.ui.main.questmanager

import ishopgo.com.exhibition.model.question.QuestionObject
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate

/**
 * Created by xuanhong on 6/14/18. HappyCoding!
 */
class QuestionConverter : Converter<QuestionObject, QuestProvider> {

    override fun convert(from: QuestionObject): QuestProvider {
        return object : QuestProvider {
            override fun provideTime(): String {
                return from.createdAt?.asDate() ?: ""
            }

            override fun provideTitle(): String {
                return from.name ?: ""
            }

        }
    }

}