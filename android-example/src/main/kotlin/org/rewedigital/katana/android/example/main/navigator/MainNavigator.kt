package org.rewedigital.katana.android.example.main.navigator

import android.app.Activity

interface MainNavigator {

    fun exit()
}

class MainNavigatorImpl(private val activity: Activity) : MainNavigator {

    override fun exit() {
        activity.finish()
    }
}
