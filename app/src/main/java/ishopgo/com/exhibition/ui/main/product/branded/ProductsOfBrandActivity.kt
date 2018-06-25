package ishopgo.com.exhibition.ui.main.product.branded

import android.os.Bundle
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductsOfBrandActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_of_brand)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_map_host_fragment).navigateUp()
    }

}