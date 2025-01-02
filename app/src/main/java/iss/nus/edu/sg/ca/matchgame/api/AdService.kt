package iss.nus.edu.sg.fragments.workshop.mobile_ca.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Response
import iss.nus.edu.sg.fragments.workshop.mobile_ca.data.AdResponse

interface AdService {
    @GET("api/ad")
    suspend fun fetchAd(@Header("User-Id") userId: String): Response<AdResponse>
}