package com.krahs.androidstafflab

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import com.krahs.androidstafflab.startuptrace.StartupEventKind
import com.krahs.androidstafflab.startuptrace.StartupEventRecorder
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StartupEventRecorder.recordOnce(StartupEventKind.ACTIVITY_ON_CREATE)
        observeFirstDraw(window.decorView)
        enableEdgeToEdge()
        setContent {
            SideEffect {
                StartupEventRecorder.recordOnce(StartupEventKind.COMPOSE_CONTENT_ENTERED)
            }
            AndroidStaffLabTheme {
                AndroidStaffLabRoot()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        StartupEventRecorder.recordOnce(StartupEventKind.ACTIVITY_ON_START)
    }

    override fun onResume() {
        super.onResume()
        StartupEventRecorder.recordOnce(StartupEventKind.ACTIVITY_ON_RESUME)
    }

    private fun observeFirstDraw(decorView: View) {
        val listener = object : ViewTreeObserver.OnDrawListener {
            override fun onDraw() {
                StartupEventRecorder.recordOnce(StartupEventKind.FIRST_FRAME_OBSERVED)
                decorView.post {
                    if (decorView.viewTreeObserver.isAlive) {
                        decorView.viewTreeObserver.removeOnDrawListener(this)
                    }
                }
            }
        }
        decorView.viewTreeObserver.addOnDrawListener(listener)
    }
}
