package com.tower.buildmaster.ror.data.repo

import android.util.Log
import com.tower.buildmaster.ror.domain.model.BuildMasterEntity
import com.tower.buildmaster.ror.domain.model.FarmDeskParam
import com.tower.buildmaster.ror.presentation.app.BuildMasterApp
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.lang.Exception

interface BuildMasterApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun getClient(
        @Body jsonString: JsonObject,
    ): Call<BuildMasterEntity>
}


private const val BM_MAIN = "https://buiildmasstter.com/"
class BuildMasterRepository {

    suspend fun chickenGetClient(
        farmDeskParam: FarmDeskParam,
        chickenConversion: MutableMap<String, Any>?
    ): BuildMasterEntity? {
        val gson = Gson()
        val api = chickenGetApi(BM_MAIN, null)

        val chickenJsonObject = gson.toJsonTree(farmDeskParam).asJsonObject
        chickenConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            chickenJsonObject.add(key, element)
        }
        return try {
            val chickenRequest: Call<BuildMasterEntity> = api.getClient(
                jsonString = chickenJsonObject,
            )
            val chickenResult = chickenRequest.awaitResponse()
            Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: Result code: ${chickenResult.code()}")
            if (chickenResult.code() == 200) {
                Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: Get request success")
                Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: Code = ${chickenResult.code()}")
                Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: ${chickenResult.body()}")
                chickenResult.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun chickenGetApi(url: String, client: OkHttpClient?) : BuildMasterApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
