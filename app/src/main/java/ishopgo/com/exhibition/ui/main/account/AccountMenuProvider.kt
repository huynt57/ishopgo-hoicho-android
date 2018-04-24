package ishopgo.com.exhibition.ui.main.account

/**
 * Created by xuanhong on 4/23/18. HappyCoding!
 */
interface AccountMenuProvider {

    fun provideIcon(): Int

    fun provideName(): String

    fun provideChilds(): List<AccountMenuProvider>

    fun provideIsParent(): Boolean

    fun provideAction(): Int

    fun provideTextColor(): Int

}