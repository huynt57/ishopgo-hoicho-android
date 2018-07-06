package ishopgo.com.exhibition.ui.base

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.FirebaseDatabase
import ishopgo.com.exhibition.BuildConfig
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppFactory
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.extensions.transact
import ishopgo.com.exhibition.ui.login.LoginActivity


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

    /**
     * require input of activity is a list of key in intent
     */
    protected open fun requireInput(): List<String> = listOf()

    // verify input of activity has all key
    protected open fun validateInput(intent: Intent?): Boolean{
        if (intent == null) return true

        var inputOK = true
        val requireInput = requireInput()
        for (s in requireInput) {
            if (!intent.hasExtra(s)) {
                inputOK = false
                break
            }
        }

        return inputOK
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (!validateInput(intent))
            throw IllegalArgumentException("Chưa truyền đủ tham số")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!validateInput(intent))
            throw IllegalArgumentException("Chưa truyền đủ tham số")

        val checkingViewModel = obtainViewModel(VersionCheckingViewModel::class.java)
        checkingViewModel
                .versionChecking(FirebaseDatabase.getInstance().getReference("expo/app_version/android"))
                .observe(this, Observer { v ->
                    v?.let {
                        val message = it.child("message")?.getValue(String::class.java) ?: ""
                        val latestVersion = it.child("ver")?.getValue(String::class.java) ?: ""
                        val latest = latestVersion.split(".")
                        Log.d("EXPO Version", "latest version = $latest")

                        val apkVersion = BuildConfig.VERSION_NAME
                        val apk = apkVersion.split(".")
                        Log.d("EXPO Version", "apk version = $apk")

                        if (apkVersion.equals(latestVersion, true)) {
                            // version valid, do nothing
                            Log.d("EXPO Version", "version is the same")
                            return@Observer
                        }

                        if (apk[0].toInt() < latest[0].toInt() || apk[1].toInt() < latest[1].toInt()) {
                            // major version is old. should force update
                            showDialogUpdateVersion(this, latestVersion, message, true)
                            return@Observer
                        }

                        if (apk[2].toInt() < latest[2].toInt()) {
                            // minor update, should message user only
                            if (!UserDataManager.skipUpdate)
                                showDialogUpdateVersion(this, latestVersion, message, false)
                            return@Observer
                        }
                    }

                })
    }

    private fun showDialogUpdateVersion(context: Context, latestVersion: String, message: String, forceUpdate: Boolean) {
        Handler().postDelayed({
            val builder = MaterialDialog.Builder(context)
            builder
                    .title("Phiên bản: $latestVersion")
                    .content(message)
                    .positiveText("Cập nhật")
                    .onPositive { dialog, _ ->
                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=hàng%20việt%20360%20ishopgo"))
                        if (intent.resolveActivity(packageManager) != null) {
                            dialog.dismiss()
                            startActivity(intent)
                            finish()
                        } else {
                            dialog.dismiss()
                            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=hàng%20việt%20360%20ishopgo&c=apps"))
                            startActivity(intent)
                            finish()
                        }
                    }
            if (!forceUpdate) {
                builder.negativeText("Để sau")
                        .onNegative { _, _ -> UserDataManager.skipUpdate = true }
                builder.cancelable(true)
            } else {
                builder.cancelable(false)
            }

            builder.show()
        }, 1500)
    }

    fun resolveError(error: BaseErrorSignal) {
        when (error.errorCode) {
            BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                Toast.makeText(this, error.errorMessage, Toast.LENGTH_SHORT).show()
            }
            BaseErrorSignal.ERROR_401 -> {
                Toast.makeText(this, "Xin vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                UserDataManager.deleteUserInfo()

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                startActivity(intent)

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