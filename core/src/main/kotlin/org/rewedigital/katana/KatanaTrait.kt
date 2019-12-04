package org.rewedigital.katana

/**
 * Helper trait for simplifying injection syntax.
 *
 * Can be applied to a class which holds a [Component].
 * The extension functions [inject], [injectNow] and [canInject] can then be used instead of explicitly referencing
 * `component` every time.
 *
 * ```
 * class Example : KatanaTrait {
 *     override val component = Component(module)
 *     // We don't have to explicitly reference component like component.inject()
 *     private val myDependency: MyDependency by inject()
 * }
 * ```
 */
interface KatanaTrait {
    val component: Component
}

/**
 * @see Component.inject
 */
inline fun <reified T> KatanaTrait.inject(name: Any? = null) =
    withComponent { inject<T>(name) }

/**
 * @see Component.injectNow
 *
 * TODO: Rename to get()?
 */
inline fun <reified T> KatanaTrait.injectNow(name: Any? = null) =
    withComponent { injectNow<T>(name) }

/**
 * @see Component.canInject
 */
inline fun <reified T> KatanaTrait.canInject(name: Any? = null) =
    withComponent { canInject<T>(name) }

@Suppress("SENSELESS_COMPARISON")
@PublishedApi
internal fun <R> KatanaTrait.withComponent(body: Component.() -> R) =
// Despite the non-null type the `component` can be null if the above extension functions are used before the
// `component` property was initialized.
    if (component == null) {
        throw ComponentNotInitializedException("component is null! Make sure to use KatanaTrait's extension functions *after* component property was initialized.")
    } else {
        body.invoke(component)
    }
