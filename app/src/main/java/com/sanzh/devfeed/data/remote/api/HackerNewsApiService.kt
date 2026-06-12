package com.sanzh.devfeed.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

// HN Firebase REST API — free, no key
interface HackerNewsApiService {
    @GET("topstories.json")
    suspend fun getTopStoryIds(): List<Long>
    @GET("item/{id}.json")
    suspend fun getStory(@Path("id") id: Long): HnStoryDto
}

data class HnStoryDto(
    @SerializedName("id") val id : Long,
    @SerializedName("title") val title : String?,
    @SerializedName("url") val url : String?,
    @SerializedName("score") val score : Int = 0,
    @SerializedName("descendants") val descendants : Int = 0,
    @SerializedName("by") val by : String = "",
    @SerializedName("time") val time : Long = 0
)