package org.rewedigital.katana.android.example

import android.app.Application
import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary
import org.rewedigital.katana.Component
import org.rewedigital.katana.Katana
import org.rewedigital.katana.android.KatanaAndroidLogger
import org.rewedigital.katana.android.example.inject.androidModule
import org.rewedigital.katana.android.modules.createApplicationModule
import org.rewedigital.katana.createComponent

class KatanaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //<editor-fold desc="Setup leak detection">
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        //</editor-fold>

        // Installing logger for Katana
        Katana.logger = KatanaAndroidLogger

        applicationComponent = createComponent(
            createApplicationModule(this),
            androidModule
        )
    }

    companion object {
        // The application component is a singleton and can be accessed by other components via
        // KatanaApp.applicationComponent.
        lateinit var applicationComponent: Component
    }
}
