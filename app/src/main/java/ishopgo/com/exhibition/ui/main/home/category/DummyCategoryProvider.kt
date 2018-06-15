package ishopgo.com.exhibition.ui.main.home.category

/**
 * Created by xuanhong on 6/15/18. HappyCoding!
 */
class DummyCategoryProvider: CategoryProvider {
    override fun provideIcon(): String {
        return ""
    }

    override fun provideName(): String {
        return "Danh mục"
    }

    override fun provideChilds(): List<CategoryProvider> {
        return listOf()
    }

    override fun provideIsParent(): Boolean {
        return true
    }

    override fun provideLevel(): Int {
        return 1
    }

    override fun provideCount(): Int {
        return 0
    }


}