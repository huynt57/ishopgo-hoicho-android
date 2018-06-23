package ishopgo.com.exhibition.model

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import ishopgo.com.exhibition.R

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
)