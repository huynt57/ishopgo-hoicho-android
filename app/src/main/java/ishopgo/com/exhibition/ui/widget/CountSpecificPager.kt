package ishopgo.com.exhibition.ui.widget

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
abstract class CountSpecificPager(fm: FragmentManager, tabCount: Int) : FragmentStatePagerAdapter(fm) {

    private var tabCount: Int = 0
    private var mFragmentManager: FragmentManager
    private val mFragments = SparseArray<Fragment>()

    init {
        this.tabCount = tabCount
        this.mFragmentManager = fm
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return tabCount
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = mFragments.get(position)
        return if (fragment == null) {
            val newItem = super.instantiateItem(container, position)
            if (newItem is Fragment)
                mFragments.put(position, newItem)

            newItem
        } else
            fragment
    }

    fun getFragment(position: Int): Fragment? {
        return mFragments.get(position)
    }
}
