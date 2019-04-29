package org.rewedigital.katana.android.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Base class for Fragments with Katana dependency injection.
 *
 * [onInject] must be implemented where component initialization and injection should take place.
 * It is called after [onActivityCreated]. For a more detailed explanation see [KatanaFragmentDelegate].
 *
 * If this base class cannot be used because the Fragment must extend from another class,
 * [KatanaFragmentDelegate] can still be used individually.
 *
 * @see KatanaFragmentDelegate
 */
abstract class KatanaFragment : Fragment() {

    private val fragmentDelegate: KatanaFragmentDelegate<KatanaFragment>

    init {
        fragmentDelegate = fragmentDelegate { activity, savedInstanceState -> onInject(activity, savedInstanceState) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentDelegate.onActivityCreated(savedInstanceState)
    }

    abstract fun onInject(activity: Activity, savedInstanceState: Bundle?)
}
