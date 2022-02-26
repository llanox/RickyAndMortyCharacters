package co.gabriel.rickyandmorty.data.network

import co.gabriel.rickyandmorty.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://rickandmortyapi.com/api/character/"

    val characterAPIService : CharacterAPIService  by lazy {
        getRetrofit().create(CharacterAPIService::class.java)
    }
    private fun getRetrofit(): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) { // development build
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else { // production build
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }

        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

}