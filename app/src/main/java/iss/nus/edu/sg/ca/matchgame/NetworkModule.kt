package iss.nus.edu.sg.ca.matchgame

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("User-Agent", "Mozilla")  // 添加User-Agent头
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("你的.NET后端URL/") // 例如: "http://10.0.2.2:5000/"
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGameApi(retrofit: Retrofit): GameApi {
        return retrofit.create(GameApi::class.java)
    }
}