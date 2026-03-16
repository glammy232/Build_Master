package com.tower.buildmaster

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.tower.buildmaster.ror.BuildMasterGlobalLayoutUtil
import com.tower.buildmaster.ror.chickenSetupSystemBars
import com.tower.buildmaster.ror.presentation.app.BuildMasterApp
import com.tower.buildmaster.ror.presentation.pushhandler.BuildMasterPushHandler
import org.koin.android.ext.android.inject

class BuildMasterActivity : AppCompatActivity() {

    private val proBubbleBoPlingPushHandler by inject<BuildMasterPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        chickenSetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_chicken)
        val chickenRootView = findViewById<View>(android.R.id.content)
        BuildMasterGlobalLayoutUtil().chickenAssistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(chickenRootView) { chickenView, chickenInsets ->
            val chickenSystemBars = chickenInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val chickenDisplayCutout = chickenInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val chickenIme = chickenInsets.getInsets(WindowInsetsCompat.Type.ime())


            val chickenTopPadding = maxOf(chickenSystemBars.top, chickenDisplayCutout.top)
            val chickenLeftPadding = maxOf(chickenSystemBars.left, chickenDisplayCutout.left)
            val chickenRightPadding = maxOf(chickenSystemBars.right, chickenDisplayCutout.right)
            window.setSoftInputMode(BuildMasterApp.Companion.chickenInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "ADJUST PUN")
                val chickenBottomInset = maxOf(chickenSystemBars.bottom, chickenDisplayCutout.bottom)

                chickenView.setPadding(chickenLeftPadding, chickenTopPadding, chickenRightPadding, 0)

                chickenView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = chickenBottomInset
                }
            } else {
                Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "ADJUST RESIZE")

                val chickenBottomInset = maxOf(chickenSystemBars.bottom, chickenDisplayCutout.bottom, chickenIme.bottom)

                chickenView.setPadding(chickenLeftPadding, chickenTopPadding, chickenRightPadding, 0)

                chickenView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = chickenBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(BuildMasterApp.Companion.BM_MAIN_TAG, "Activity onCreate()")
        proBubbleBoPlingPushHandler.chickenHandlePush(intent.extras)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            chickenSetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        chickenSetupSystemBars()
    }
}