package com.tower.buildmaster.ror.domain.model

import com.google.gson.annotations.SerializedName


private const val BM_A = "com.tower.buildmaster"
data class FarmDeskParam (
    @SerializedName("af_id")
    val chickenAfId: String,
    @SerializedName("bundle_id")
    val chickenBundleId: String = BM_A,
    @SerializedName("os")
    val chickenOs: String = "Android",
    @SerializedName("store_id")
    val chickenStoreId: String = BM_A,
    @SerializedName("locale")
    val chickenLocale: String,
    @SerializedName("push_token")
    val chickenPushToken: String,
    @SerializedName("firebase_project_id")
    val chickenFirebaseProjectId: String = "buildmaster-d7736"

    )