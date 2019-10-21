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
        viewModelStoreOwner: ViewModelStoreOwner,
        savedStateRegistryOwner: SavedStateRegistryOwner,
        key: String?,
        defaultArgs: Bundle?,
        viewModelClass: Class<VM>,
        viewModelProvider: (state: SavedStateHandle) -> ViewModel
    ) =
        ViewModelProvider(
            viewModelStoreOwner,
            KatanaSavedStateViewModelFactory(
                savedStateRegistryOwner,
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

    data class Arg(
        val key: String?,
        val viewModelStoreOwner: ViewModelStoreOwner,
        val savedStateRegistryOwner: SavedStateRegistryOwner
    )

    override operator fun invoke(context: ComponentContext, arg: Any?): VM =
        when (arg) {
            is Arg -> InternalSavedStateViewModelProvider.of(
                viewModelStoreOwner = arg.viewModelStoreOwner,
                savedStateRegistryOwner = arg.savedStateRegistryOwner,
                key = arg.key,
                defaultArgs = defaultArgs,
                viewModelClass = viewModelClass,
                viewModelProvider = { state -> body.invoke(ProviderDsl(context), state) }
            )
            else -> throw KatanaException(message = "Unknown argument passed to SavedStateViewModelKatanaProvider")
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
) =
    custom(
        name = viewModelName(modelClass = VM::class.java, key = key),
        provider = SavedStateViewModelKatanaProvider(
            body = body,
            defaultArgs = defaultArgs,
            viewModelClass = VM::class.java
        )
    )

/**
 * Provides [ViewModel] instance with a [SavedStateHandle] declared in current injection context.
 */
inline fun <reified VM : ViewModel, OWNER> ProviderDsl.viewModelSavedState(owner: OWNER, key: String? = null)
        where OWNER : ViewModelStoreOwner, OWNER : SavedStateRegistryOwner =
    context.custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg(
            key = key,
            viewModelStoreOwner = owner,
            savedStateRegistryOwner = owner
        )
    )

/**
 * Provides [Fragment] [ViewModel] instance with a [SavedStateHandle] scoped to its [FragmentActivity] declared in current injection context.
 */
@Deprecated(
    message = "Use viewModelSavedState(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModelSavedState(fragment.requireActivity())",
        "org.rewedigital.katana.androidx.viewmodel.savedstate.viewModelSavedState"
    )
)
inline fun <reified VM : ViewModel> ProviderDsl.activityViewModelSavedState(fragment: Fragment, key: String? = null) =
    viewModelSavedState<VM, FragmentActivity>(owner = fragment.requireActivity(), key = key)
//</editor-fold>

//<editor-fold desc="Injection extension functions">
/**
 * Immediately injects [ViewModel] with a [SavedStateHandle].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, OWNER> Component.viewModelSavedStateNow(owner: OWNER, key: String? = null)
        where OWNER : ViewModelStoreOwner, OWNER : SavedStateRegistryOwner =
    custom<VM>(
        name = viewModelName(VM::class.java, key),
        arg = Arg(
            key = key,
            viewModelStoreOwner = owner,
            savedStateRegistryOwner = owner
        )
    )

/**
 * Injects [ViewModel] with a [SavedStateHandle].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, OWNER> Component.viewModelSavedState(owner: OWNER, key: String? = null)
        where OWNER : ViewModelStoreOwner, OWNER : SavedStateRegistryOwner =
    lazy { viewModelSavedStateNow<VM, OWNER>(owner, key) }

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of current owner implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedStateNow(key: String? = null)
        where T : KatanaTrait, T : ViewModelStoreOwner, T : SavedStateRegistryOwner =
    component.viewModelSavedStateNow<VM, T>(this, key)

/**
 * Injects [ViewModel] with a [SavedStateHandle] of current owner implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.viewModelSavedState(key: String? = null)
        where T : KatanaTrait, T : ViewModelStoreOwner, T : SavedStateRegistryOwner =
    component.viewModelSavedState<VM, T>(this, key)

/**
 * Immediately injects [ViewModel] with a [SavedStateHandle] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
@Deprecated(
    message = "Use viewModelSavedStateNow(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModelSavedStateNow(owner = fragment.requireActivity(), key = key)",
        "org.rewedigital.katana.androidx.viewmodel.savedstate.viewModelSavedStateNow"
    )
)
inline fun <reified VM : ViewModel> Component.activityViewModelSavedStateNow(fragment: Fragment, key: String? = null) =
    viewModelSavedStateNow<VM, FragmentActivity>(
        owner = fragment.requireActivity(),
        key = key
    )

/**
 * Injects [ViewModel] with a [SavedStateHandle] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
@Deprecated(
    message = "Use viewModelSavedState(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModelSavedState(owner = fragment.requireActivity(), key = key)",
        "org.rewedigital.katana.androidx.viewmodel.savedstate.viewModelSavedState"
    )
)
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
    component.viewModelSavedStateNow<VM, FragmentActivity>(owner = requireActivity(), key = key)

/**
 * Injects [ViewModel] with a [SavedStateHandle] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModelSavedState
 */
inline fun <reified VM : ViewModel, T> T.activityViewModelSavedState(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModelSavedState<VM, FragmentActivity>(owner = requireActivity(), key = key)
//<editor-fold>
