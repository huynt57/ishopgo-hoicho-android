package ishopgo.com.exhibition.ui.base

import android.app.ProgressDialog
import android.arch.lifecycle.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppFactory
import ishopgo.com.exhibition.app.MyApp
import ishopgo.com.exhibition.domain.BaseErrorSignal
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.login.LoginActivity

/**
 * @author Steve
 * @since 10/22/17
 */
open class BaseFragment : Fragment() {

    /**
     * require input of activity is a list of key in intent
     */
    protected open fun requireInput(): List<String> = listOf()

    // verify input of activity has all key
    protected open fun inputVerified(argument: Bundle?): Boolean {
        if (argument == null) return true

        var inputOK = true
        val requireInput = requireInput()
        for (s in requireInput) {
            if (!argument.containsKey(s)) {
                inputOK = false
                break
            }
        }

        return inputOK
    }

    protected var reloadData = false
    protected var toast: Toast? = null
    protected var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if (!inputVerified(arguments))
            throw IllegalArgumentException("Chưa truyền đủ tham số")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // prevent click through fragment
//        view.setOnTouchListener { _, _ -> true }

        viewLifeCycleOwner = ViewLifeCyclerOwner()
        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    fun showProgressDialog(): ProgressDialog? {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(activity, null, getString(R.string.waiting), true)
        } else {
            progressDialog!!.show()
        }
        return progressDialog
    }

    fun toast(msg: String) {
        context?.let {
            if (toast == null) {
                toast = Toast.makeText(it, msg, Toast.LENGTH_SHORT)
                toast!!.show()
            } else {
                toast!!.cancel()
                toast = Toast.makeText(it, msg, Toast.LENGTH_SHORT)
                toast!!.show()
            }
        }
    }

    fun resolveError(error: BaseErrorSignal) {
        activity?.let {
            when (error.errorCode) {
                BaseErrorSignal.ERROR_HAS_MESSAGE -> {
                    Toast.makeText(it, error.errorMessage, Toast.LENGTH_SHORT).show()
                }
                BaseErrorSignal.ERROR_401 -> {
                    Toast.makeText(it, "Xin vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()

                    val intent = Intent(it, LoginActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                    it.startActivity(intent)

                    it.finish()
                    it.finishAffinity()
                }
                BaseErrorSignal.ERROR_NETWORK -> {
                    val snackBar = Snackbar.make(it.findViewById(android.R.id.content), R.string.error_no_connection, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.WHITE)
                            .setDuration(10 * 1000)
                    val snackBarText = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                    snackBarText.setTextColor(Color.WHITE)
                    snackBar.show()
                }
                BaseErrorSignal.ERROR_SERVER -> {
                    MaterialDialog.Builder(it)
                            .content("Lỗi hệ thống. Liên lạc với nhà quản trị để được hỗ trợ.")
                            .positiveText("OK")
                            .show()
                }
                BaseErrorSignal.ERROR_UNKNOWN -> {
                    MaterialDialog.Builder(it)
                            .content("Có lỗi xảy ra. Liên lạc với nhà quản trị để được hỗ trợ.")
                            .positiveText("OK")
                            .show()
                }
                else -> {
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onResume() {
        super.onResume()

        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onPause() {
        super.onPause()

        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()

        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewLifeCycleOwner?.lifecycle?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>, activityObserver: Boolean = true) =
            if (activityObserver && activity != null) ViewModelProviders.of(activity!!, AppFactory(activity!!.application as MyApp)).get(viewModelClass)
            else ViewModelProviders.of(this, AppFactory(activity?.application as MyApp)).get(viewModelClass)

    class ViewLifeCyclerOwner: LifecycleOwner {
        private val lifeCycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): LifecycleRegistry {
            return lifeCycleRegistry
        }

    }

    var viewLifeCycleOwner: ViewLifeCyclerOwner? = null

}