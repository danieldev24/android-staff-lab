package com.krahs.androidstafflab

import android.app.Application
import com.krahs.androidstafflab.startuptrace.StartupEventKind
import com.krahs.androidstafflab.startuptrace.StartupEventRecorder

class AndroidStaffLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        StartupEventRecorder.recordOnce(StartupEventKind.APPLICATION_ON_CREATE)
    }
}
