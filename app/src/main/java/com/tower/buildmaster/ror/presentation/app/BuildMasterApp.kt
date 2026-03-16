package com.tower.buildmaster.ror.presentation.app

import android.app.Application
import android.util.Log
import android.view.WindowManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.tower.buildmaster.ror.presentation.di.buildmasterModule
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


sealed interface BuildMasterAppsFlyerState {
    data object BuildMasterDefault : BuildMasterAppsFlyerState
    data class BuildMasterSuccess(val chickenData: MutableMap<String, Any>?) :
        BuildMasterAppsFlyerState
    data object BuildMasterError : BuildMasterAppsFlyerState
}

interface BuildMasterAppsApi {
    @Headers("Content-Type: application/json")
    @GET(BM_LIN)
    fun chickenGetClient(
        @Query("devkey") devkey: String,
        @Query("device_id") deviceId: String,
    ): Call<MutableMap<String, Any>?>
}
private const val BM_APP_DEV = "7dSpbxFhnPDVc65unCjTXU"
private const val BM_LIN = "com.tower.buildmaster"
class BuildMasterApp : Application() {
    private var chickenIsResumed = false

    override fun onCreate() {
        super.onCreate()

        val appsflyer = AppsFlyerLib.getInstance()
        appsflyer.setDebugLog(true)
        chickenSetDebufLogger(appsflyer)
        chickenMinTimeBetween(appsflyer)
        appsflyer.setDebugLog(true)

        appsflyer.init(
            BM_APP_DEV,
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    Log.d(BM_MAIN_TAG, "onConversionDataSuccess: $p0")

                    val afStatus = p0?.get("af_status")?.toString() ?: "null"
                    if (afStatus == "Organic") {
                        chickenResume(BuildMasterAppsFlyerState.BuildMasterError)
                        /*CoroutineScope(Dispatchers.IO).launch {
                            try {
                                delay(5000)
                                val api = chickenGetApi(
                                    "https://gcdsdk.appsflyer.com/install_data/v4.0/",
                                    null
                                )
                                val response = api.chickenGetClient(
                                    devkey = FARMDESK_APP_DEV,
                                    deviceId = chickenGetAppsflyerId()
                                ).awaitResponse()

                                val resp = response.body()
                                Log.d(FARM_MAIN_TAG, "After 5s: $resp")
                                if (resp?.get("af_status") == "Organic") {
                                    chickenResume(FarmDeskAppsFlyerState.FarmDeskSuccess(resp))
                                } else {
                                    chickenResume(
                                        FarmDeskAppsFlyerState.FarmDeskSuccess(resp)
                                    )
                                }
                            } catch (d: Exception) {
                                Log.d(FARM_MAIN_TAG, "Error: ${d.message}")
                                chickenResume(FarmDeskAppsFlyerState.FarmDeskError)
                            }
                        }*/
                    } else {
                        chickenResume(BuildMasterAppsFlyerState.BuildMasterSuccess(p0))
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                    Log.d(BM_MAIN_TAG, "onConversionDataFail: $p0")
                    chickenResume(BuildMasterAppsFlyerState.BuildMasterError)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    Log.d(BM_MAIN_TAG, "onAppOpenAttribution")
                }

                override fun onAttributionFailure(p0: String?) {
                    Log.d(BM_MAIN_TAG, "onAttributionFailure: $p0")
                }
            },
            this
        )

        appsflyer.start(this, BM_APP_DEV, object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d(BM_MAIN_TAG, "AppsFlyer started")
            }

            override fun onError(p0: Int, p1: String) {
                Log.d(BM_MAIN_TAG, "AppsFlyer start error: $p0 - $p1")
                chickenResume(BuildMasterAppsFlyerState.BuildMasterError)
            }
        })
        
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BuildMasterApp)
            modules(
                listOf(
                    buildmasterModule
                )
            )
        }
    }

    private fun chickenResume(state: BuildMasterAppsFlyerState) {
        if (!chickenIsResumed) {
            chickenIsResumed = true
            chickenConversionFlow.value = state
        }
    }

    private fun chickenGetAppsflyerId(): String {
        val appsflyrid = AppsFlyerLib.getInstance().getAppsFlyerUID(this) ?: ""
        Log.d(BM_MAIN_TAG, "AppsFlyer: AppsFlyer Id = $appsflyrid")
        return appsflyrid
    }

    private fun chickenSetDebufLogger(appsflyer: AppsFlyerLib) {
        appsflyer.setDebugLog(true)
    }

    private fun chickenMinTimeBetween(appsflyer: AppsFlyerLib) {
        appsflyer.setMinTimeBetweenSessions(0)
    }

    private fun chickenGetApi(url: String, client: OkHttpClient?) : BuildMasterAppsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }

    companion object {
        var chickenInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        val chickenConversionFlow: MutableStateFlow<BuildMasterAppsFlyerState> = MutableStateFlow(
            BuildMasterAppsFlyerState.BuildMasterDefault
        )
        var BM_FB_LI: String? = null
        const val BM_MAIN_TAG = "BMMainTag"
    }
}