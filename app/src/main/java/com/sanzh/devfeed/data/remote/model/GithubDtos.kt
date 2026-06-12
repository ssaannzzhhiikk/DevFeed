package com.sanzh.devfeed.data.remote.model

import com.google.gson.annotations.SerializedName
import com.sanzh.devfeed.domain.model.GithubRepo

data class GithubSearchResponse(
    @SerializedName("items") val items: List<GithubRepoDto>
)

data class GithubRepoDto(
    @SerializedName("id") val id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("full_name") val fullName : String,
    @SerializedName("description") val description : String?,
    @SerializedName("language") val language : String?,
    @SerializedName("stargazers_count") val stars : Int,
    @SerializedName("forks_count") val forks : Int,
    @SerializedName("html_url") val htmlUrl : String,
    @SerializedName("owner") val owner : GithubOwnerDto
)

data class GithubOwnerDto(
    @SerializedName("avatar_url") val avatarUrl: String
)