package ishopgo.com.exhibition.ui.main.home.post

/**
 * Created by xuanhong on 6/13/18. HappyCoding!
 */
interface LatestPostProvider {
    fun provideAvatar(): CharSequence
    fun provideTitle(): CharSequence
    fun provideTime(): CharSequence
    fun provideShortDescription(): CharSequence
}