package iss.nus.edu.sg.ca.matchgame

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class AdFragment : Fragment() {
    private lateinit var adView: AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }
}