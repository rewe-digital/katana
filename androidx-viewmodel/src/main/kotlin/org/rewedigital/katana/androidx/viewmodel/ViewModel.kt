@file:Suppress("unused")

package org.rewedigital.katana.androidx.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.ModuleBindingContext
import org.rewedigital.katana.androidx.viewmodel.internal.viewModelName
import org.rewedigital.katana.dsl.ProviderDsl
import org.rewedigital.katana.dsl.factory

//<editor-fold desc="Internal utility functions and classes">
@Suppress("UNCHECKED_CAST")
@PublishedApi
internal class KatanaViewModelProviderFactory(private val viewModelProvider: () -> ViewModel) :
    ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>) =
        viewModelProvider() as VM
}

@PublishedApi
internal inline fun <reified VM : ViewModel> ViewModelProvider.get(key: String?) =
    when (key) {
        null -> get(VM::class.java)
        else -> get(key, VM::class.java)
    }

@PublishedApi
internal object InternalViewModelProvider {

    inline fun <reified VM : ViewModel> of(
        owner: ViewModelStoreOwner,
        key: String?,
        noinline viewModelProvider: () -> ViewModel
    ) =
        ViewModelProvider(
            owner,
            KatanaViewModelProviderFactory(viewModelProvider)
        ).get<VM>(key)
}
//</editor-fold>

//<editor-fold desc="Module DSL">
/**
 * Declares a [ViewModel] dependency binding.
 *
 * Body of provider syntax facilitates arbitrary dependency injection into the ViewModel when first instantiated.
 * **Important:** The ViewModel instance is handled by AndroidX's [ViewModelProvider] and not by Katana itself.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ProviderDsl.viewModel
 * @see ProviderDsl.activityViewModel
 */
inline fun <reified VM : ViewModel> ModuleBindingContext.viewModel(
    key: String? = null,
    noinline body: ProviderDsl.() -> VM
) =
    factory(name = viewModelName(modelClass = VM::class.java, key = key), body = body)

/**
 * Provides [ViewModel] instance of given [ViewModelStoreOwner] declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.viewModel(owner: ViewModelStoreOwner, key: String? = null) =
    InternalViewModelProvider.of<VM>(owner, key) {
        context.injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Provides [Fragment] [ViewModel] instance scoped to its [FragmentActivity] declared in current injection context.
 */
@Deprecated(
    message = "Use viewModel(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModel(owner = fragment.requireActivity(), key = key)",
        "org.rewedigital.katana.androidx.viewmodel.viewModel"
    )
)
inline fun <reified VM : ViewModel> ProviderDsl.activityViewModel(fragment: Fragment, key: String? = null) =
    viewModel<VM>(owner = fragment.requireActivity(), key = key)
//</editor-fold>

//<editor-fold desc="Injection extension functions">
/**
 * Immediately injects [ViewModel] of specified [ViewModelStoreOwner].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModelNow(owner: ViewModelStoreOwner, key: String? = null) =
    InternalViewModelProvider.of<VM>(owner, key) {
        injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Injects [ViewModel] of specified [ViewModelStoreOwner].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModel(owner: ViewModelStoreOwner, key: String? = null) =
    lazy { viewModelNow<VM>(owner, key) }

/**
 * Immediately injects [ViewModel] of current [ViewModelStoreOwner] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModelNow(key: String? = null) where T : KatanaTrait, T : ViewModelStoreOwner =
    component.viewModelNow<VM>(this, key)

/**
 * Injects [ViewModel] of current [ViewModelStoreOwner] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModel(key: String? = null) where T : KatanaTrait, T : ViewModelStoreOwner =
    component.viewModel<VM>(this, key)

/**
 * Immediately injects [ViewModel] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
@Deprecated(
    message = "Use viewModelNow(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModelNow(owner = fragment.requireActivity(), key = key)",
        "org.rewedigital.katana.androidx.viewmodel.viewModelNow"
    )
)
inline fun <reified VM : ViewModel> Component.activityViewModelNow(fragment: Fragment, key: String? = null) =
    viewModelNow<VM>(owner = fragment.requireActivity(), key = key)

/**
 * Injects [ViewModel] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
@Deprecated(
    message = "Use viewModel(ViewModelStoreOwner) passing the Activity",
    replaceWith = ReplaceWith(
        "viewModelNow(owner = fragment.requireActivity(), key = key)",
        "org.rewedigital.katana.androidx.viewmodel.viewModel"
    )
)
inline fun <reified VM : ViewModel> Component.activityViewModel(fragment: Fragment, key: String? = null) =
    lazy { activityViewModelNow<VM>(fragment, key) }

/**
 * Immediately injects [ViewModel] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.activityViewModelNow(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModelNow<VM>(owner = requireActivity(), key = key)

/**
 * Injects [ViewModel] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.activityViewModel(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModel<VM>(owner = requireActivity(), key = key)
//</editor-fold>
