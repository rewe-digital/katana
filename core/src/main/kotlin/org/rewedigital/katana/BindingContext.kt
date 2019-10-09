package org.rewedigital.katana

import org.rewedigital.katana.dsl.ModuleDslMarker
import java.util.concurrent.atomic.AtomicInteger

abstract class BindingContext {
    internal abstract val module: Module
    internal abstract val key: Key?
    internal abstract val increment: Int?
}

@ModuleDslMarker
class ModuleBindingContext internal constructor(
    @PublishedApi override val module: Module
) : BindingContext() {

    override val key: Key? = null
    override val increment: Int? = null
}

@ModuleDslMarker
@Suppress("unused")
class SetBindingContext<T> @PublishedApi internal constructor(
    override val module: Module,
    @PublishedApi override val key: Key
) : BindingContext() {

    override val increment
        get() = nextIncrement.getAndIncrement()

    private companion object {
        private val nextIncrement = AtomicInteger()
    }
}
