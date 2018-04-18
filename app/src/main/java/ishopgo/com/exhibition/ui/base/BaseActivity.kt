package ishopgo.com.exhibition.ui.base

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppFactory
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.extensions.transact

/**
 * @author Steve
 * @since 10/22/17
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        onBackPressed()
        return true
    }

    fun resolveError(error: BaseErrorSignal) {
        when (error.errorCode) {
            BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                Toast.makeText(this, error.errorMessage, Toast.LENGTH_SHORT).show()
            }
            BaseErrorSignal.ERROR_401 -> {
                Toast.makeText(this, "Xin vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                finish()
                finishAffinity()
            }
            BaseErrorSignal.ERROR_NETWORK -> {
                val snackBar = Snackbar.make(findViewById(android.R.id.content), R.string.error_no_connection, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.WHITE)
                        .setDuration(10 * 1000)
                val snackBarText = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                snackBarText.setTextColor(Color.WHITE)
                snackBar.show()
            }
            BaseErrorSignal.ERROR_SERVER -> {
                MaterialDialog.Builder(this)
                        .content("Có lỗi xảy ra, đã báo cáo về ban quản trị. Xin vui lòng thử lại sau.")
                        .positiveText("OK")
                        .onPositive { _, _ -> finish(); finishAffinity() }
                        .show()
            }
            BaseErrorSignal.ERROR_UNKNOWN -> {
                MaterialDialog.Builder(this)
                        .content("Có lỗi xảy ra, đã báo cáo về ban quản trị. Xin vui lòng thử lại sau.")
                        .positiveText("OK")
                        .cancelable(false)
                        .onPositive { dialog, _ -> dialog.dismiss(); finish() }
                        .show()
            }
            else -> {
            }
        }

    }

    fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>) =
            ViewModelProviders.of(this, AppFactory(application as MyApp)).get(viewModelClass)

    fun replaceFragmentInActivity(fragment: Fragment, frameId: Int, isAddToBackStack: Boolean = false, tag: String? = null) {
        supportFragmentManager.transact {
            replace(frameId, fragment, tag ?: fragment.javaClass.name)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (isAddToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
        }
    }
}