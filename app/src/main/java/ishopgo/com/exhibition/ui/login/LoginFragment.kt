package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.survey.SurveyActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException
import com.facebook.GraphRequest
import ishopgo.com.exhibition.ui.login.facebook.UpdateInfoLoginActivity


/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginFragment : BaseFragment() {
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var viewModel: LoginViewModel
    private var phone = ""

    companion object {
        const val TAG = "LoginFragment"
        fun newInstance(phone: String): LoginFragment {
            val f = LoginFragment()
            f.phone = phone
            return f
        }

        val NOT_SURVEY = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_forget_password.text = "<b>Quên mật khẩu</b>".asHtml()
        tv_signup.text = "<b>Đăng ký</b>".asHtml()
        btn_signup.text = "Chưa có tài khoản ? <b><font color=\"#ff3d00\">Đăng ký ngay</font></b>".asHtml()

        tv_signup.setOnClickListener {
            val intent = Intent(context, SignupActivity::class.java)
            startActivity(intent)
        }

        tv_skip_login.setOnClickListener {
            // we pass login screen after click skip
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
            activity?.finish()
        }

        btn_login.setOnClickListener {
            if (isRequiredFieldsValid(tv_account.text.toString(), tv_password.text.toString())) {
                showProgressDialog()
                viewModel.loginAccount(tv_account.text.toString(), tv_password.text.toString())
            }
        }

        btn_login_fb.setOnClickListener {
            loginFb()
        }

        tv_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                if (tv_password != null)
                    tv_password.hideKeyboard()

                if (isRequiredFieldsValid(tv_account.text.toString(), tv_password.text.toString())) {
                    showProgressDialog()
                    viewModel.loginAccount(tv_account.text.toString(), tv_password.text.toString())
                }
                return@OnEditorActionListener true
            }

            false
        })

        tv_forget_password.setOnClickListener {
            val intent = Intent(context, ForgetActivity::class.java)
            startActivity(intent)
        }

        if (phone.isNotEmpty()) {
            tv_account.setText(phone)
        }
    }

    private fun loginFb() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onError(error: FacebookException?) {
                Toast.makeText(context, "Có lỗi xảy ra. Hãy thử lại!", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                Toast.makeText(context, "Hãy đăng nhập facebook tham gia hội chợ", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult?) {
                val parameters = Bundle()
                parameters.putString("fields", "id, name, email, picture.type(large)")

                val gr = GraphRequest.newMeRequest(
                        result?.accessToken) { json, response ->
                    if (response.error != null) {
                        // handle error
                        Log.d("ERROR FaceBook", response.error.toString())
                    } else {
                        //System.out.println("Success");
                        try {
                            val geoObject = response.jsonObject

//                            val jsonresult = json.toString()
//                            println("JSON resp$jsonresult")
//                            val resp = response.jsonObject.toString()
//                            println("JSON resp $resp")
                            val id = geoObject.getString("id")

                            val email = if (geoObject.has("email"))
                                geoObject.getString("email") ?: ""
                            else ""

                            val name =  if (geoObject.has("name"))
                                geoObject.getString("name") ?: ""
                            else ""

                            val avatar = if (geoObject.has("picture")) {
                                geoObject.getJSONObject("picture").getJSONObject("data").getString("url")
                                        ?: "https://graph.facebook.com/$id/picture"
                            } else "https://graph.facebook.com/$id/picture"
//                            val link = geoObject.getString("user_link") ?:""
                            viewModel.loginFacebook(id, email, avatar, "", name)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
                gr.parameters = parameters
                gr.executeAsync()
            }
        })
        LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("public_profile"))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.loginSuccess.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                toast("Đăng nhập thành công")
                if (UserDataManager.currentSurveyIsMandatory) {
                    if (UserDataManager.currentType == "Thành viên")
                        viewModel.checkSurvey()
                    else {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        })

        viewModel.loginFacebookUpdate.observe(this, Observer {
            val intent = Intent(context, UpdateInfoLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })

        viewModel.loginFacebookSuccess.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                toast("Đăng nhập thành công")
                if (UserDataManager.currentSurveyIsMandatory) {
                    if (UserDataManager.currentType == "Thành viên")
                        viewModel.checkSurvey()
                    else {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        })

        viewModel.checkSurveySusscess.observe(this, Observer { p ->
            p.let {
                if (it == NOT_SURVEY) {
                    toast("Bạn chưa làm bài khảo sát")
                    val intent = Intent(context, SurveyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, "")
                    startActivity(intent)
                    activity?.finish()
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                    startActivity(intent)
                    activity?.finishAffinity()
                }
            }
        })
    }

    private fun isRequiredFieldsValid(name: String, title: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tài khoản không được để trống")
            tv_account.error = "Trường này còn trống"
            return false
        }

        if (title.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_password.error = "Trường này còn trống"
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}