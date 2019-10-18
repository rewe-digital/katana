@file:Suppress("FunctionName")

package org.rewedigital.katana.android.modules

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.factory
import org.rewedigital.katana.dsl.singleton

const val APPLICATION = "APPLICATION"
const val APPLICATION_CONTEXT = "APPLICATION_CONTEXT"

/**
 * Provides a named binding of `Application` as `APPLICATION` and of the application `Context` as `APPLICATION_CONTEXT`.
 */
fun ApplicationModule(app: Application) = Module("ApplicationModule") {

    singleton(name = APPLICATION) { app }

    singleton<Context>(name = APPLICATION_CONTEXT) { app }
}

@Deprecated(
    message = "Use ApplicationModule()",
    replaceWith = ReplaceWith(
        "ApplicationModule(app)",
        "org.rewedigital.katana.android.modules.ApplicationModule"
    )
)
fun createApplicationModule(app: Application) = ApplicationModule(app)

const val ACTIVITY = "ACTIVITY"
const val ACTIVITY_CONTEXT = "ACTIVITY_CONTEXT"

/**
 * Provides a named binding of `Activity` as `ACTIVITY` and of the Activity's `Context` as `ACTIVITY_CONTEXT`.
 *
 * **Note:** This module should only be referenced from a [Component][org.rewedigital.katana.Component] located in an
 * `Activity` or else Activity/memory leaks might be introduced!
 */
fun ActivityModule(activity: Activity) = Module("ActivityModule") {

    singleton(name = ACTIVITY) { activity }

    singleton<Context>(name = ACTIVITY_CONTEXT) { activity }
}

@Deprecated(
    message = "Use ActivityModule()",
    replaceWith = ReplaceWith(
        "ActivityModule(activity)",
        "org.rewedigital.katana.android.modules.ActivityModule"
    )
)
fun createActivityModule(activity: Activity) = ActivityModule(activity)

const val SUPPORT_FRAGMENT = "SUPPORT_FRAGMENT"
const val SUPPORT_FRAGMENT_CONTEXT = "SUPPORT_FRAGMENT_CONTEXT"

/**
 * Provides a named binding of `Fragment` as `SUPPORT_FRAGMENT` and of the Fragment's `Context` as
 * `SUPPORT_FRAGMENT_CONTEXT`. Note that the Fragment's Context may be `null`.
 */
fun SupportFragmentModule(fragment: Fragment) = Module("SupportFragmentModule") {

    singleton(name = SUPPORT_FRAGMENT) { fragment }

    factory(name = SUPPORT_FRAGMENT_CONTEXT) { fragment.context }
}

@Deprecated(
    message = "Use SupportFragmentModule()",
    replaceWith = ReplaceWith(
        "SupportFragmentModule(fragment)",
        "org.rewedigital.katana.android.modules.SupportFragmentModule"
    )
)
fun createSupportFragmentModule(fragment: Fragment) = SupportFragmentModule(fragment)
