package ishopgo.com.exhibition.ui.widget

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.content_toolbar.view.*

/**
 * Created by xuanhong on 8/17/17. HappyCoding!
 */

class AppToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.content_toolbar, this)
        showDivider(true)
    }

    fun setCustomTitle(title: CharSequence) {
        view_toolbar_title.setText(title)
    }

    fun getTitleView() = view_toolbar_title

    fun showDivider(isShow: Boolean) {
        view_divider_toolbar_bottom.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun leftButton(@DrawableRes icon: Int = R.drawable.ic_arrow_back_24dp, count: Int = 0) {
        if (icon == 0) {
            view_option_left.visibility = View.GONE
            view_toolbar_start.setOnClickListener(null)
            view_toolbar_start.setImageDrawable(null)
        } else {
            view_option_left.visibility = View.VISIBLE
            view_toolbar_start.setImageDrawable(AppCompatResources.getDrawable(context, icon))
        }

        if (count > 0) {
            tv_unread_count_left.visibility = View.VISIBLE
            tv_unread_count_left.text = count.toString()
        } else
            tv_unread_count_left.visibility = View.GONE
    }

    fun setLeftButtonClickListener(listener: (v: View) -> Unit) {
        view_toolbar_start.setOnClickListener { listener.invoke(it) }
    }

    fun setRightButtonClickListener(listener: (v: View) -> Unit) {
        view_option_end.setOnClickListener { listener.invoke(it) }
    }

    fun setRight2ButtonClickListener(listener: (v: View) -> Unit) {
        view_option_end2.setOnClickListener { listener.invoke(it) }
    }

    fun rightButton(@DrawableRes icon: Int, count: Int = 0) {
        if (icon == 0) {
            view_option_end.visibility = View.VISIBLE
            view_option_end.setOnClickListener(null)
        } else
            view_option_end.visibility = View.VISIBLE
        Glide.with(context).load(icon).into(view_toolbar_end)

        if (count > 0) {
            tv_unread_count.visibility = View.VISIBLE
            tv_unread_count.text = count.toString()
        } else
            tv_unread_count.visibility = View.GONE

    }

    fun rightButton2(@DrawableRes icon: Int, count: Int = 0) {
        if (icon == 0) {
            view_option_end2.visibility = View.VISIBLE
            view_option_end2.setOnClickListener(null)
        } else view_option_end2.visibility = View.VISIBLE
        Glide.with(context).load(icon).into(view_toolbar_end2)

        if (count > 0) {
            tv_unread_count2.visibility = View.VISIBLE
            tv_unread_count2.text = count.toString()
        } else
            tv_unread_count2.visibility = View.GONE

    }

}
