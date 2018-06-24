package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.RatingProductFragment
import ishopgo.com.exhibition.ui.main.product.detail.RatingProductViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ProductCommentsActivity : BaseSingleFragmentActivity() {
    private lateinit var ratingViewModel: RatingProductViewModel

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductCommentsFragmentActionBar.newInstance(startupOption)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingViewModel = obtainViewModel(RatingProductViewModel::class.java)
        ratingViewModel.isCommentEnable.observe(this, Observer {
            it?.let { it1 -> openRatingComment(it1) }
        })
    }

    private fun openRatingComment(product: ProductDetail) {
        val params = Bundle()
        params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        val transaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_container, RatingProductFragment.newInstance(params))
                .addToBackStack(RatingProductFragment.TAG)
        transaction.commit()
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

}