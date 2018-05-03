package ishopgo.com.exhibition.ui.photoview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import kotlinx.android.synthetic.main.activity_photo_list_view.*
import kotlinx.android.synthetic.main.fragment_photo_album_view.*

class PhotoAlbumViewActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_list_view)

        val imagesUri = intent?.getStringArrayExtra(Const.TransferKey.EXTRA_STRING_LIST)
                ?: arrayOf<String>()

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, imagesUri)

        // Set up the ViewPager with the sections adapter.
        view_pager.adapter = mSectionsPagerAdapter
        view_pager.offscreenPageLimit = 3

        circle_indicator.setViewPager(view_pager)

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager, private var imagesUri: Array<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(imagesUri[position])
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return imagesUri.size
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_photo_album_view, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val url = arguments?.getString(ARG_URL) ?: ""
            Glide.with(this).load(url).apply(RequestOptions()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)).into(iv_image)
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_URL = "url"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(url: String): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putString(ARG_URL, url)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
