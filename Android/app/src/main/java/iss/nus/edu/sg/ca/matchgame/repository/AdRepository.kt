package iss.nus.edu.sg.fragments.workshop.mobile_ca.repository

import iss.nus.edu.sg.fragments.workshop.mobile_ca.api.AdService
import iss.nus.edu.sg.fragments.workshop.mobile_ca.data.AdResponse
import javax.inject.Inject

class AdRepository @Inject constructor(private val adService: AdService) {
    suspend fun fetchAd(userId: String): Result<AdResponse> = try {
        val response = adService.fetchAd(userId)
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Failed to fetch ad: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
