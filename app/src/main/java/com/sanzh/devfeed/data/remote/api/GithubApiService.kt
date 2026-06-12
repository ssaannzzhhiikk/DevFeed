package com.sanzh.devfeed.data.remote.api

import com.sanzh.devfeed.data.remote.model.GithubRepoDto
import com.sanzh.devfeed.data.remote.model.GithubSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order : String = "desc",
        @Query("per_page") perPage : Int = 25
    ) : GithubSearchResponse

    @GET("repositories/{id}")
    suspend fun getRepository(@Path("id") id: Long): GithubRepoDto
}