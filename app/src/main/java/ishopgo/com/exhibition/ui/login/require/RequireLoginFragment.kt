package ishopgo.com.exhibition.ui.login.require

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_require_login.*

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class RequireLoginFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_require_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_login.setOnClickListener {
            val intent = Intent(it.context, LoginActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
        }
    }
}