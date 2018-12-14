package org.rewedigital.katana

import org.rewedigital.katana.Katana.Logger
import org.rewedigital.katana.Katana.environmentContext
import org.rewedigital.katana.Katana.logger
import org.rewedigital.katana.environment.DefaultEnvironmentContext
import org.rewedigital.katana.environment.EnvironmentContext

/**
 * Global Katana configuration object.
 *
 * Provides the possibility to specify a [Logger] via [logger] and an [EnvironmentContext] via [environmentContext]
 * for configuring Katana for a specific runtime environment.
 *
 * @see Logger
 * @see logger
 * @see EnvironmentContext
 * @see environmentContext
 */
object Katana {

    interface Logger {

        fun debug(msg: String)

        fun info(msg: String)

        fun warn(msg: String)

        fun error(msg: String, throwable: Throwable? = null)
    }

    /**
     * Pass an implementation of [Logger] here to enable Katana's logging functionality
     */
    var logger: Logger? = null

    /**
     * Configures Katana for a specific runtime environment.
     *
     * This property must be set very early in the application lifecycle. Preferably before any modules and components
     * are created.
     */
    var environmentContext: EnvironmentContext = DefaultEnvironmentContext
}
