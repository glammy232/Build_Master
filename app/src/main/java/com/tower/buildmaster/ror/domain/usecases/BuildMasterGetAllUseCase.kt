package com.tower.buildmaster.ror.domain.usecases

import android.util.Log
import com.tower.buildmaster.ror.data.repo.BuildMasterRepository
import com.tower.buildmaster.ror.data.utils.BuildMasterPushToken
import com.tower.buildmaster.ror.data.utils.BuildMasterSystemService
import com.tower.buildmaster.ror.domain.model.BuildMasterEntity
import com.tower.buildmaster.ror.domain.model.FarmDeskParam
import com.tower.buildmaster.ror.presentation.app.BuildMasterApp

class BuildMasterGetAllUseCase(
    private val buildMasterRepository: BuildMasterRepository,
    private val volcanoSystemService: BuildMasterSystemService,
    private val aeroMarinePushToken: BuildMasterPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : BuildMasterEntity?{
        val params = FarmDeskParam(
            chickenLocale = volcanoSystemService.volcanoGetLocale(),
            chickenPushToken = aeroMarinePushToken.chickenGetToken(),
            chickenAfId = volcanoSystemService.volcanoGetAppsflyerId()
        )
        Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Params for request: $params")
        return buildMasterRepository.chickenGetClient(params, conversion)
    }



}