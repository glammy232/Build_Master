package com.tower.buildmaster.ror.presentation.ui.view


import android.webkit.PermissionRequest

interface BuildMasterCallBack {
    fun chickenHandleCreateWebWindowRequest(proBubbleBoPlingVi: BuildMasterVi)

    fun chickenOnPermissionRequest(todoSphereRequest: PermissionRequest?)

    fun chickenOnFirstPageFinished()
}