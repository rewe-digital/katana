package org.rewedigital.katana.androidx.fragment

import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaException

/**
 * A [FragmentFactory] that utilizes Katana for [Fragment] instance resolution.
 *
 * The factory can only provide instances of Fragments that have been registered via [handlesFragment] or
 * [handlesFragmentVia]. The referred component(s) must provide bindings for these Fragments.
 *
 * ```
 * val module = Module {
 *
 *   singleton {
 *       KatanaFragmentFactory(component)
 *           .handlesFragment<FirstFragment>()
 *           .handlesFragment<SecondFragment>(name = "SecondFragment")
 *   }
 *
 *   factory { FirstFragment(get()) }
 *
 *   factory(name = "SecondFragment") { SecondFragment(get(), get()) }
 * }
 * ```
 *
 * @param defaultComponent Optional default [Component] required for [handlesFragment]
 * @param delegateToSuper If `true`, will delegate requests to super FragmentFactory if this factory cannot resolve Fragment. Might be useful for third-party Fragments like `NavHostFragment` for instance.
 *
 * @see handlesFragment
 * @see handlesFragmentVia
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class KatanaFragmentFactory(
    defaultComponent: Component? = null,
    private val delegateToSuper: Boolean = true
) : FragmentFactory() {

    @PublishedApi
    internal val defaultComponentProvider =
        defaultComponent?.let { { it } }

    @PublishedApi
    internal val providers = ArrayMap<String, () -> Fragment>()

    /**
     * Declare that this factory handles Fragment instantiations of given type.
     *
     * A default [Component] **must** be passed to constructor of factory in order for this to work.
     * The default component must provide a binding for the requested Fragment which is bound to the class or
     * optional name. For instance a module might declare:
     *
     * ```
     * factory { MyFragment(get(), get()) }
     * ```
     *
     * Use [handlesFragmentVia] for adding a Fragment provider associated with a different component.
     *
     * @see handlesFragmentVia
     */
    inline fun <reified T : Fragment> handlesFragment(
        name: String? = null
    ): KatanaFragmentFactory {
        defaultComponentProvider?.let { component ->
            val fragmentClassName = T::class.java.name
            providers[fragmentClassName] = { component().injectNow<T>(name = name) }
        } ?: throw KatanaException("No default component passed to constructor of KatanaFragmentFactory")
        return this
    }

    /**
     * Declare that this factory handles Fragment instantiations via provided [Component].
     *
     * The supplied [Component] must provide a binding for the requested Fragment which is bound to the class or
     * optional name. For instance a module might declare:
     *
     * ```
     * factory { MyFragment(get(), get()) }
     * ```
     *
     * @see handlesFragment
     */
    inline fun <reified T : Fragment> handlesFragmentVia(
        name: String? = null,
        noinline component: () -> Component
    ): KatanaFragmentFactory {
        val fragmentClassName = T::class.java.name
        providers[fragmentClassName] = { component().injectNow<T>(name = name) }
        return this
    }

    /**
     * Create a new instance of a Fragment with the given class name.
     *
     * If this Fragment has not been registered via [handlesFragment] or [handlesFragmentVia] and [delegateToSuper] is `true`,
     * will call super [FragmentFactory] for resolution. Else throws [KatanaException].
     *
     * @throws KatanaException
     */
    override fun instantiate(classLoader: ClassLoader, className: String) =
        providers[className]?.let { provider ->
            provider()
        } ?: if (delegateToSuper)
            super.instantiate(
                classLoader,
                className
            ) else throw KatanaException("No Fragment provider found for class $className (delegateToSuper == false)")
}
