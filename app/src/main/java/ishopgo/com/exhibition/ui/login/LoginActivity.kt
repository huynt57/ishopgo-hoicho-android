package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.survey.SurveyActivity

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginActivity : BaseSingleFragmentActivity() {
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val require = intent?.getBooleanExtra(Const.TransferKey.EXTRA_REQUIRE, false) ?: false
        if (require) {
        } else
            if (UserDataManager.currentSurvey <= 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                startActivity(intent)
                finishAffinity()
            } else
                if (UserDataManager.currentUserId > 0) {
                    if (UserDataManager.currentSurveyIsMandatory) {
                        if (UserDataManager.currentType == "Thành viên") {
                            if (UserDataManager.currentSurvey == 0) {
                                val intent = Intent(this, SurveyActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, "")
                                startActivity(intent)
                                finishAffinity()
                            } else {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                startActivity(intent)
                                finishAffinity()
                            }
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                            startActivity(intent)
                            finishAffinity()
                        }
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        startActivity(intent)
                        finishAffinity()
                    }
                }

    }

    override fun createFragment(startupOption: Bundle): Fragment {
        val phone = intent.getStringExtra("phone")
        return if (phone == null || phone == "") LoginFragment.newInstance("")
        else LoginFragment.newInstance(phone)
    }
}