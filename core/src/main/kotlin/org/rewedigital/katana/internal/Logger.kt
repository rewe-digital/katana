package org.rewedigital.katana.internal

import org.rewedigital.katana.Katana

internal object Logger {

    fun debug(msg: () -> String) {
        Katana.logger?.debug(msg())
    }

    fun info(msg: () -> String) {
        Katana.logger?.info(msg())
    }

    fun warn(msg: () -> String) {
        Katana.logger?.warn(msg())
    }

    fun error(msgAndThrowable: () -> Pair<String, Throwable?>) {
        Katana.logger?.let { logger ->
            val pair = msgAndThrowable()
            logger.error(pair.first, pair.second)
        }
    }
}
