package org.rewedigital.katana.android.modules

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import org.rewedigital.katana.createModule
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton

const val APPLICATION = "APPLICATION"
const val APPLICATION_CONTEXT = "APPLICATION_CONTEXT"

/**
 * Provides a named binding of `Application` as `APPLICATION` and of the application `Context` as `APPLICATION_CONTEXT`.
 */
fun createApplicationModule(app: Application) = createModule("applicationModule") {

    singleton(name = APPLICATION) { app }

    singleton<Context>(name = APPLICATION_CONTEXT) { app }
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

    singleton(name = ACTIVITY) { activity }

    singleton<Context>(name = ACTIVITY_CONTEXT) { activity }
}

const val SUPPORT_FRAGMENT = "SUPPORT_FRAGMENT"
const val SUPPORT_FRAGMENT_CONTEXT = "SUPPORT_FRAGMENT_CONTEXT"

/**
 * Provides a named binding of `Fragment` as `SUPPORT_FRAGMENT` and of the Fragment's `Context` as
 * `SUPPORT_FRAGMENT_CONTEXT`. Note that the Fragment's Context may be `null`.
 */
fun createSupportFragmentModule(fragment: Fragment) = createModule("supportFragmentModule") {

    singleton(name = SUPPORT_FRAGMENT) { fragment }

    factory(name = SUPPORT_FRAGMENT_CONTEXT) { fragment.context }
}
