package com.android.roundup

import android.app.Application

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
