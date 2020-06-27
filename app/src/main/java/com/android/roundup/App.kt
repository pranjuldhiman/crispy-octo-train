package com.android.roundup

import android.app.Application
import com.splunk.mint.Mint

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Mint.initAndStartSession(this, "4d934f8d")
        mSelf = this
    }

    companion object {
        private var mSelf: App? = null

        fun self(): App {
            return mSelf!!
        }
    }
}
