package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdFragment : Fragment() {
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
=======
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class AdFragment : Fragment() {
    private lateinit var adView: AdView
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
<<<<<<< HEAD
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
=======
        Log.d("AdFragment", "onCreateView called")

        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_ad, container, false)

        // Find the AdView by its ID from the layout
        adView = view.findViewById(R.id.adView)

        // Only load the ad after AdMob SDK initialization
        MobileAds.initialize(requireContext()) { initializationStatus ->
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        adView.destroy()  // Clean up the ad when the fragment view is destroyed
>>>>>>> b3768c3f6b28ca4b770fcfa8d9abbab105baf6b4
    }
}