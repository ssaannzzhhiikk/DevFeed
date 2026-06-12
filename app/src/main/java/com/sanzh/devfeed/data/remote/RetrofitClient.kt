package com.sanzh.devfeed.data.remote

import com.sanzh.devfeed.data.remote.api.GithubApiService
import com.sanzh.devfeed.data.remote.api.HackerNewsApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder().build()

    val githubApi: GithubApiService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApiService::class.java)
    val hnApi: HackerNewsApiService = Retrofit.Builder()
        .baseUrl("https://hacker-news.firebaseio.com/v0/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HackerNewsApiService::class.java)
}