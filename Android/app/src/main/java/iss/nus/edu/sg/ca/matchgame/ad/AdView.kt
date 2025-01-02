package iss.nus.edu.sg.fragments.workshop.mobile_ca.ui.ad

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import iss.nus.edu.sg.ca.matchgame.databinding.ViewAdBinding
import iss.nus.edu.sg.fragments.workshop.mobile_ca.repository.AdRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import iss.nus.edu.sg.fragments.workshop.mobile_ca.api.RetrofitClient

class AdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAdBinding.inflate(LayoutInflater.from(context), this, true)
    private var adUpdateJob: Job? = null

    fun startAdRotation(scope: CoroutineScope, userId: String) {
        adUpdateJob?.cancel()
        adUpdateJob = scope.launch {
            while (isActive) {
                try {
                    val ad = AdRepository(RetrofitClient.adService).fetchAd(userId)
                    ad.onSuccess { adResponse ->
                        updateAdContent(adResponse.content)
                    }
                } catch (e: Exception) {
                    Log.e("AdView", "Error fetching ad", e)
                }
                delay(30_000) // 30 seconds delay
            }
        }
    }

    private fun updateAdContent(content: String) {
        binding.adContainer.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                binding.adText.text = content
                binding.adContainer.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    fun stopAdRotation() {
        adUpdateJob?.cancel()
        adUpdateJob = null
    }
}