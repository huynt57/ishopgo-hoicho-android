package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ProductCommentsFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): ProductCommentsFragmentActionBar {
            val f = ProductCommentsFragmentActionBar()
            f.arguments = params

            return f
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommentFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Bình luận sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}