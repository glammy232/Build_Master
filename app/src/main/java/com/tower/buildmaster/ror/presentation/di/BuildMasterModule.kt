package com.tower.buildmaster.ror.presentation.di

import com.tower.buildmaster.ror.data.repo.BuildMasterRepository
import com.tower.buildmaster.ror.data.shar.BuildMasterSharedPreference
import com.tower.buildmaster.ror.data.utils.BuildMasterPushToken
import com.tower.buildmaster.ror.data.utils.BuildMasterSystemService
import com.tower.buildmaster.ror.domain.usecases.BuildMasterGetAllUseCase
import com.tower.buildmaster.ror.presentation.pushhandler.BuildMasterPushHandler
import com.tower.buildmaster.ror.presentation.ui.load.BuildMasterLoadViewModel
import com.tower.buildmaster.ror.presentation.ui.view.BuildMasterViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val buildmasterModule = module {
    factory {
        BuildMasterPushHandler()
    }
    single {
        BuildMasterRepository()
    }
    single {
        BuildMasterSharedPreference(get())
    }
    factory {
        BuildMasterPushToken()
    }
    factory {
        BuildMasterSystemService(get())
    }
    factory {
        BuildMasterGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        BuildMasterViFun(get())
    }
    viewModel {
        BuildMasterLoadViewModel(
            get(),
            get(),
            get()
        )
    }
}