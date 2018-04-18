package ishopgo.com.exhibition.ui.base.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActivity


class SplashActivity : BaseActivity() {

    private var handler = Handler()
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = obtainViewModel(SplashViewModel::class.java)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
    }

}
