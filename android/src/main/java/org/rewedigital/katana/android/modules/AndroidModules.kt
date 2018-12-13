package org.rewedigital.katana.android.modules

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.factory
import org.rewedigital.katana.singleton

const val APPLICATION = "APPLICATION"
const val APPLICATION_CONTEXT = "APPLICATION_CONTEXT"

/**
 * Provides a named binding of `Application` as `APPLICATION` and of the application `Context` as `APPLICATION_CONTEXT`.
 */
fun createApplicationModule(app: Application) = createModule("applicationModule") {

    bind<Application>(APPLICATION) { singleton { app } }

    bind<Context>(APPLICATION_CONTEXT) { singleton { app } }
}

const val ACTIVITY = "ACTIVITY"
const val ACTIVITY_CONTEXT = "ACTIVITY_CONTEXT"

/**
 * Provides a named binding of `Activity` as `ACTIVITY` and of the Activity's `Context` as `ACTIVITY_CONTEXT`.
 *
 * **Note:** This module should only be referenced from a [Component][org.rewedigital.katana.Component] located in an
 * `Activity` or else Activity/memory leaks might be introduced!
 */
fun createActivityModule(activity: Activity) = createModule("activityModule") {

    bind<Activity>(ACTIVITY) { singleton { activity } }

    bind<Context>(ACTIVITY_CONTEXT) { singleton { activity } }
}

const val SUPPORT_FRAGMENT = "SUPPORT_FRAGMENT"
const val SUPPORT_FRAGMENT_CONTEXT = "SUPPORT_FRAGMENT_CONTEXT"

/**
 * Provides a named binding of `Fragment` as `SUPPORT_FRAGMENT` and of the Fragment's `Context` as
 * `SUPPORT_FRAGMENT_CONTEXT`. Note that the Fragment's Context may be `null`.
 */
fun createSupportFragmentModule(fragment: Fragment) = createModule("supportFragmentModule") {

    bind<Fragment>(SUPPORT_FRAGMENT) { singleton { fragment } }

    bind<Context?>(SUPPORT_FRAGMENT_CONTEXT) { factory { fragment.context } }
}
