package ishopgo.com.exhibition.ui.main.home

import android.view.View
import ishopgo.com.exhibition.ui.base.BaseFragment

abstract class HomeFragmentOverwrite {
    abstract fun handleInOtherFlavor(rootView: View, viewModel: HomeViewModel, fragment: BaseFragment)
}