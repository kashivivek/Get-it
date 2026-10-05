package com.getit.getit.yes

import android.app.Application
import com.getit.getit.yes.util.ThemePrefs

class GetItApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemePrefs.apply(this)
    }
}
