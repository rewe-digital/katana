package org.rewedigital.katana.androidx.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
        fragment: Fragment,
        key: String?,
        noinline viewModelProvider: () -> ViewModel
    ) =
        ViewModelProviders.of(
            fragment,
            KatanaViewModelProviderFactory(viewModelProvider)
        ).get<VM>(key)

    inline fun <reified VM : ViewModel> of(
        activity: FragmentActivity,
        key: String?,
        noinline viewModelProvider: () -> ViewModel
    ) =
        ViewModelProviders.of(
            activity,
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
) {
    factory(name = viewModelName(modelClass = VM::class.java, key = key), body = body)
}

/**
 * Provides [Fragment] [ViewModel] instance declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.viewModel(fragment: Fragment, key: String? = null) =
    InternalViewModelProvider.of<VM>(fragment, key) {
        context.injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Provides [Fragment] [ViewModel] instance scoped to its [FragmentActivity] declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.activityViewModel(fragment: Fragment, key: String? = null) =
    InternalViewModelProvider.of<VM>(fragment.requireActivity(), key) {
        context.injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Provides [FragmentActivity] [ViewModel] instance declared in current injection context.
 */
inline fun <reified VM : ViewModel> ProviderDsl.viewModel(activity: FragmentActivity, key: String? = null) =
    InternalViewModelProvider.of<VM>(activity, key) {
        context.injectNow(name = viewModelName(VM::class.java, key))
    }
//</editor-fold>

//<editor-fold desc="Injection extension functions">
/**
 * Immediately injects [ViewModel] of specified [Fragment].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModelNow(fragment: Fragment, key: String? = null) =
    InternalViewModelProvider.of<VM>(fragment, key) {
        injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Injects [ViewModel] of specified [Fragment].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModel(fragment: Fragment, key: String? = null) =
    lazy { viewModelNow<VM>(fragment, key) }

/**
 * Immediately injects [ViewModel] of current [Fragment] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModelNow(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModelNow<VM>(this, key)

/**
 * Injects [ViewModel] of current [Fragment] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModel(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.viewModel<VM>(this, key)

/**
 * Immediately injects [ViewModel] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.activityViewModelNow(fragment: Fragment, key: String? = null) =
    InternalViewModelProvider.of<VM>(fragment.requireActivity(), key) {
        injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Injects [ViewModel] of specified [Fragment] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
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
    component.activityViewModelNow<VM>(this, key)

/**
 * Injects [ViewModel] of current [Fragment] implementing [KatanaTrait] scoped to its [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.activityViewModel(key: String? = null) where T : KatanaTrait, T : Fragment =
    component.activityViewModel<VM>(this, key)

/**
 * Immediately injects [ViewModel] of specified [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModelNow(activity: FragmentActivity, key: String? = null) =
    InternalViewModelProvider.of<VM>(activity, key) {
        injectNow(name = viewModelName(VM::class.java, key))
    }

/**
 * Injects [ViewModel] of specified [FragmentActivity].
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel> Component.viewModel(activity: FragmentActivity, key: String? = null) =
    lazy { viewModelNow<VM>(activity, key) }

/**
 * Immediately injects [ViewModel] of current [FragmentActivity] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModelNow(key: String? = null) where T : KatanaTrait, T : FragmentActivity =
    component.viewModelNow<VM>(this, key)

/**
 * Injects [ViewModel] of current [FragmentActivity] implementing [KatanaTrait] interface.
 *
 * @param key Optional key as required for `ViewModelProvider.get(String, Class)`
 *
 * @see ModuleBindingContext.viewModel
 */
inline fun <reified VM : ViewModel, T> T.viewModel(key: String? = null) where T : KatanaTrait, T : FragmentActivity =
    component.viewModel<VM>(this, key)
//</editor-fold>
