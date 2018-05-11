package ishopgo.com.exhibition.ui.main.home.category

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
interface CategoryProvider {

    fun provideIcon(): String

    fun provideName(): String

    fun provideChilds(): List<CategoryProvider>

    fun provideIsParent(): Boolean

    fun provideLevel(): Int

}