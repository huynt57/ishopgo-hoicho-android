package ishopgo.com.exhibition.ui.main.postmanager.detail

/**
 * Created by xuanhong on 6/14/18. HappyCoding!
 */
interface PostManagerDetailProvider {

    fun provideTitle(): CharSequence
    fun provideInfo(): CharSequence
    fun provideCategory(): CharSequence

}