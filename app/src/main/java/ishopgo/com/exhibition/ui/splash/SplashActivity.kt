package ishopgo.com.exhibition.ui.splash

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity


class SplashActivity : BaseActivity() {

    private var handler = Handler()
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = obtainViewModel(SplashViewModel::class.java)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        handler.postDelayed({

            UserDataManager.currentUserAvatar = "https://lh3.googleusercontent.com/-B212qdip-ls/V1a_aouWG1I/AAAAAAAAAfA/pqqn9gV9tcIos_ybMhM_xLmFORG_ZHEowCEwYBhgL/w278-h280-p/10275486_934954516552276_3867031212727261639_o.jpg"
            UserDataManager.currentUserName = "Vương Xuân Hồng"
            UserDataManager.currentUserPhone = "0974427143"
            UserDataManager.currentUserId = 10L

            val intent = Intent(this@SplashActivity, LoginSelectOptionActivity::class.java)
            startActivity(intent)
            finish()

        }, 500)
    }

}
