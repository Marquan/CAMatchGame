package iss.nus.edu.sg.fragments.workshop.mobile_ca.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5000/" // 如果在模拟器上测试，这是访问本地主机的地址
    // 如果是实际的服务器地址，替换成你的服务器地址

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val adService: AdService by lazy {
        retrofit.create(AdService::class.java)
    }
}

