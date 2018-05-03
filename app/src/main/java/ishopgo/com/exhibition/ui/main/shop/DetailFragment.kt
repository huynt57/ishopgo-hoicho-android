package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.product.shop.ProductsFragment
import ishopgo.com.exhibition.ui.main.shop.category.CategoryFragment
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoFragment
import ishopgo.com.exhibition.ui.main.shop.rate.RateFragment
import ishopgo.com.exhibition.ui.widget.CountSpecificPager
import kotlinx.android.synthetic.main.fragment_shop_detail.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class DetailFragment : BaseFragment() {

    private lateinit var viewModel: ShopDetailViewModel
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetailAdapter(childFragmentManager)
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 4
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view_tab_layout))
        view_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ShopDetailViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.shopImage.observe(this, Observer { i ->
            i?.let {
                Glide.with(view_image.context)
                        .load(it)
                        .apply(RequestOptions().centerInside())
                        .into(view_image)
            }
        })
    }

    class DetailAdapter(fm: FragmentManager) : CountSpecificPager(fm, 4) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ShopInfoFragment.newInstance(Bundle())
                }
                1 -> {
                    ProductsFragment()
                }
                2 -> {
                    CategoryFragment()
                }
                3 -> {
                    RateFragment()
                }
                else -> {
                    Fragment()
                }
            }
        }

    }
}