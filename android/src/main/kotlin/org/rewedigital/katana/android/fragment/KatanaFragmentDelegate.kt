package org.rewedigital.katana.android.fragment

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Usually Katana components of Fragments depend on components of the parent Activity.
 *
 * Since Fragments are instantiated **before** Activities, the component initialization must be
 * delayed until the Activity was created.
 *
 * The delegate simplifies this procedure a bit. A delegate is for example created in the init block
 * of the Fragment via [fragmentDelegate]. [onActivityCreated] of the delegate must be called in
 * [Fragment.onActivityCreated]. The lambda passed to the delegate is called when [onActivityCreated]
 * was called. Inside the lambda component initialization and dependency injection should be
 * performed.
 *
 * @see fragmentDelegate
 * @see KatanaFragment
 */
class KatanaFragmentDelegate<T : Fragment>(
    private val fragment: T,
    private val onInject: T.(activity: Activity) -> Unit
) {

    /**
     * Must be called in [Fragment.onActivityCreated] of Fragment associated with this delegate.
     */
    fun onActivityCreated() {
        fragment.activity?.let { activity ->
            fragment.onInject(activity)
        }
            ?: throw IllegalStateException("Activity is null. Was delegate called in onActivityCreated() of Fragment?")
    }
}

fun <T : Fragment> T.fragmentDelegate(onInject: T.(activity: Activity) -> Unit) =
    KatanaFragmentDelegate(
        fragment = this,
        onInject = onInject
    )
