package ishopgo.com.exhibition.ui.base

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import ishopgo.com.exhibition.R
import kotlinx.android.synthetic.main.activity_single_navigation.*

abstract class BaseNavigationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_navigation)

        if (navigationRes() <= 0)
            throw IllegalArgumentException("Navigation không hợp lệ")

        val navHostFragment = nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(navigationRes())

        graph.setDefaultArguments(startArguments())

        navHostFragment.navController.graph = graph
    }

    abstract fun navigationRes(): Int

    protected open fun startArguments(): Bundle = Bundle()

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }
}