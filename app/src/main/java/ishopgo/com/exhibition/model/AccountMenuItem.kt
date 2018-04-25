package ishopgo.com.exhibition.model

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.main.account.AccountMenuProvider

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 *
 * Dummy class help to create menu item
 */
data class AccountMenuItem(
        val action: Int,
        @DrawableRes val icon: Int,
        val name: String,
        val childs: List<AccountMenuItem>? = null,
        val isParent: Boolean = true,
        @ColorRes val textColor: Int = R.color.md_grey_800
) : AccountMenuProvider {

    override fun provideTextColor(): Int {
        return textColor
    }

    override fun provideIcon(): Int {
        return icon
    }

    override fun provideName(): String {
        return name
    }

    override fun provideChilds(): List<AccountMenuProvider> {
        return childs ?: listOf()
    }

    override fun provideIsParent(): Boolean {
        return isParent
    }

    override fun provideAction(): Int {
        return action
    }

}