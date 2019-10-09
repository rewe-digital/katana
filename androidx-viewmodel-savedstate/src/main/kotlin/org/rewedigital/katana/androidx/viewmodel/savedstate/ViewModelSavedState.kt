@file:Suppress("unused")

package org.rewedigital.katana.androidx.viewmodel.savedstate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import org.rewedigital.katana.*
import org.rewedigital.katana.androidx.viewmodel.internal.viewModelName
import org.rewedigital.katana.androidx.viewmodel.savedstate.SavedStateViewModelKatanaProvider.Arg
import org.rewedigital.katana.dsl.ProviderDsl
import org.rewedigital.katana.dsl.custom

internal class KatanaSavedStateViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    private val viewModelProvider: (state: SavedStateHandle) -> ViewModel
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(key: String, modelClass: Class<VM>, handle: SavedStateHandle): VM =
        viewModelProvider(handle) as VM
}

internal object InternalSavedStateViewModelProvider {

    fun <VM : ViewModel> of(
        fragment: Fragment,
        key: String?,
        defaultArgs: Bundle?,
        viewModelClass: Class<VM>,
        viewModelProvider: (state: SavedStateHandle) -> ViewModel
    ) =
        ViewModelProviders.of(
            fragment,
            KatanaSavedStateViewModelFactory(
                fragment,
                defaultArgs,
                viewModelProvider
            )
        ).internalGet(key, viewModelClass)

    fun <VM : ViewModel> of(
        activity: FragmentActivity,
        key: String?,
        defaultArgs: Bundle?,
        viewModelClass: Class<VM>,
        viewModelProvider: (state: SavedStateHandle) -> ViewModel
    ) =
        ViewModelProviders.of(
            activity,
            KatanaSavedStateViewModelFactory(
                activity,
                defaultArgs,
                viewModelProvider
            )
        ).internalGet(key, viewModelClass)

    private fun <VM : ViewModel> ViewModelProvider.internalGet(key: String?, viewModelClass: Class<VM>) =
        if (key == null)
            get(viewModelClass)
        else
            get(key, viewModelClass)
}

@PublishedApi
internal class SavedStateViewModelKatanaProvider<VM : ViewModel>(
    private val body: ProviderDsl.(state: SavedStateHandle) -> VM,
    private val defaultArgs: Bundle?,
    private val viewModelClass: Class<VM>
) : Provider<VM> {

    sealed class Arg(val key: String?) {

        class Fragment(key: String?, val fragment: androidx.fragment.app.Fragment) : Arg(key)

        class Activity(key: String?, val activity: FragmentActivity) : Arg(key)
    }

    override operator fun invoke(context: ComponentContext, arg: Any?): VM =
        when (arg) {
            is Arg.Fragment -> InternalSavedStateViewModelProvider.of(
                fragment = arg.fragment,
                key = arg.key,
                defaultArgs = defaultArgs,
                viewModelClass = viewModelClass,
                viewModelProvider = { state -> body.invoke(ProviderDsl(context), state) }
            )
            is Arg.Activity -> InternalSavedStateViewModelProvider.of(
                activity = arg.activity,
                key = arg.key,
                defaultArgs = defaultArgs,
                viewModelClass = viewModelClass,
                viewModelProvider = { state -> body.invoke(ProviderDsl(context), state) }
            )
            else -> throw KatanaException(message = "Missing or unhandled SavedStateVMKatanaProvider.Arg instance")
        }
}

//<editor-fold desc="Module DSL">
/**
 * Declares a [ViewModel] dependency binding with a [SavedStateHandle].
 *
 * The saved state is passed to the provider body as an argument which then can be passed to the ViewModel instance
 * when created.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 * @param defaultArgs Optional default args that are passed to [AbstractSavedStateViewModelFactory]
 */
inline fun <reified VM : ViewModel> ModuleBindingContext.viewModelSavedState(
    key: String? = null,
    defaultArgs: Bundle? = null,
    noinline body: ProviderDsl.(state: SavedStateHandle) -> VM
) {
    custom(
        name = viewModelName(modelClass = VM::class.java, key = key),
        provider = SavedStateViewModelKatanaProvider(
            body = body,
            defaultArgs = defaultArgs,
            viewModelClass = VM::class.java
        )
    )
}

/**
 * Provides [Fragment] [ViewModel] instance with a [SavedStateHandle] declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.viewModelSavedState(fragment: Fragment, key: String? = null) =
    context.custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Fragment(key = key, fragment = fragment)
    )

/**
 * Provides [Fragment] [ViewModel] instance with a [SavedStateHandle] scoped to its [FragmentActivity] declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.activityViewModelSavedState(fragment: Fragment, key: String? = null) =
    context.custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Activity(key = key, activity = fragment.requireActivity())
    )

/**
 * Provides [FragmentActivity] [ViewModel] instance with a [SavedStateHandle] declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.viewModelSavedState(activity: FragmentActivity, key: String? = null) =
    context.custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Activity(key = key, activity = activity)
    )
//</editor-fold>

//<editor-fold desc="Injection extension functions">
/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of specified [Fragment].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.viewModelSavedStateNow(fragment: Fragment, key: String? = null) =
    custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Fragment(key = key, fragment = fragment)
    )

/**
 * Injects [ViewModel] with a [SavedStateHandle] of specified [Fragment].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.viewModelSavedState(fragment: Fragment, key: String? = null) =
    lazy { viewModelSavedStateNow<VM>(fragment, key) }

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of current [Fragment] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedStateNow(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModelSavedStateNow<VM>(this, key)

/**
 * Injects [ViewModel] with a [SavedStateHandle] of current [Fragment] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedState(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModelSavedState<VM>(this, key)

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.activityViewModelSavedStateNow(fragment: Fragment, key: String? = null) =
    custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Activity(key = key, activity = fragment.requireActivity())
    )

/**
 * Injects [ViewModel] with a [SavedStateHandle] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.activityViewModelSavedState(fragment: Fragment, key: String? = null) =
    lazy { activityViewModelSavedStateNow<VM>(fragment, key) }

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.activityViewModelSavedStateNow(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.activityViewModelSavedStateNow<VM>(this, key)

/**
 * Injects [ViewModel] with a [SavedStateHandle] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.activityViewModelSavedState(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.activityViewModelSavedState<VM>(this, key)

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of specified [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.viewModelSavedStateNow(activity: FragmentActivity, key: String? = null) =
    custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg.Activity(key = key, activity = activity)
    )

/**
 * Injects [ViewModel] with a [SavedStateHandle] of specified [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel> Component.viewModelSavedState(activity: FragmentActivity, key: String? = null) =
    lazy { viewModelSavedStateNow<VM>(activity, key) }

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of current [FragmentActivity] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedStateNow(key: String? = null) where T : KatanaTrait, T : FragmentActivity =
    component.viewModelSavedStateNow<VM>(this, key)

/**
 * Injects [ViewModel] with a [SavedStateHandle] of current [FragmentActivity] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedState(key: String? = null) where T : KatanaTrait, T : FragmentActivity =
    component.viewModelSavedState<VM>(this, key)
//<editor-fold>
