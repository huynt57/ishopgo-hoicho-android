package ishopgo.com.exhibition.ui.main.map


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_expo_map.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpoMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ExpoMapFragment : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_expo_map
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        Glide.with(view.context)
                .load("http://www.visitsingapore.com/content/dam/MICE/Global/plan-your-event/venues/Singapore-EXPO/ExpoFE_Map_Jun3_2014-page-001.jpg")
                .apply(RequestOptions()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .dontTransform()
                )
                .into(expo_map)
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Sơ đồ hội chợ")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpoMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ExpoMapFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
