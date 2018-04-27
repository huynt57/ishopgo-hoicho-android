package ishopgo.com.exhibition.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_select_option_login.*

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginSelectOptionFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_sigup.setOnClickListener {
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .customView(R.layout.dialog_select_register, false)
                        .autoDismiss(false)
                        .canceledOnTouchOutside(true)
                        .build()

                val tv_register_member = dialog.findViewById(R.id.tv_register_member) as TextView
                val tv_register_store = dialog.findViewById(R.id.tv_register_store) as TextView

                tv_register_member.setOnClickListener {
                    val intent = Intent(context, SignupActivity::class.java)
                    intent.putExtra("TYPE_REGISTER", REGISTER_MEMBER)
                    startActivity(intent)
                    dialog.dismiss()
                }

                tv_register_store.setOnClickListener {
                    val intent = Intent(context, SignupActivity::class.java)
                    intent.putExtra("TYPE_REGISTER", REGISTER_STORE)
                    startActivity(intent)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        tv_skip_login.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {
        const val REGISTER_STORE = 0
        const val REGISTER_MEMBER = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_option_login, container, false)
    }
}