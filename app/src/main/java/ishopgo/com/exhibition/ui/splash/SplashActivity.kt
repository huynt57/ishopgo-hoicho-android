package ishopgo.com.exhibition.ui.splash

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.login.LoginViewModel
import ishopgo.com.exhibition.ui.main.MainActivity


class SplashActivity : BaseActivity() {

    private var handler = Handler()
    private lateinit var viewModel: SplashViewModel
    private lateinit var viewModelLogin: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = obtainViewModel(SplashViewModel::class.java)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModelLogin = obtainViewModel(LoginViewModel::class.java)
        viewModelLogin.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        handler.postDelayed({
            if (UserDataManager.currentUserId > 0) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)
                finish()
            }

        }, 500)
    }

}
