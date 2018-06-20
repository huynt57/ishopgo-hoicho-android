package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.transact

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailActivity : BaseSingleFragmentActivity() {
    companion object {
        private val TAG = "ProductDetailActivity"
    }

    private lateinit var ratingViewModel: RatingProductViewModel

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductDetailFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingViewModel = obtainViewModel(RatingProductViewModel::class.java)
        ratingViewModel.isCommentEnable.observe(this, Observer {
            it?.let { it1 -> openRatingComment(it1) }
        })
    }

    private fun openRatingComment(product: ProductDetailProvider) {
        val params = Bundle()
        params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        val transaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .add(R.id.fragment_container, RatingProductFragment.newInstance(params))
                .addToBackStack(RatingProductFragment.TAG)
        transaction.commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: intent = [${intent}]")

        supportFragmentManager.transact {
            setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            add(R.id.fragment_container, ProductDetailFragmentActionBar.newInstance(intent?.extras
                    ?: Bundle()))
            addToBackStack(null)
        }
    }


}