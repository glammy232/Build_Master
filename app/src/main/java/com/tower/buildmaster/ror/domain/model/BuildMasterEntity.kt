package com.tower.buildmaster.ror.domain.model

import com.google.gson.annotations.SerializedName


data class BuildMasterEntity (
    @SerializedName("ok")
    val chickenOk: String,
    @SerializedName("url")
    val chickenUrl: String,
    @SerializedName("expires")
    val chickenExpires: Long,
)