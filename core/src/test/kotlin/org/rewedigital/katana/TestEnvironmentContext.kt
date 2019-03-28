package org.rewedigital.katana

import org.rewedigital.katana.environment.EnvironmentContext
import org.rewedigital.katana.environment.MapFactory

/**
 * Ensures a defined order of dependency declarations resulting in stable and predictable tests by configuring Katana
 * to use a [LinkedHashMap].
 */
object TestEnvironmentContext : EnvironmentContext {

    private class TestMapFactory : MapFactory {

        override fun <K, V> create(initialCapacity: Int?): MutableMap<K, V> =
            when (initialCapacity) {
                null -> LinkedHashMap()
                else -> LinkedHashMap(initialCapacity)
            }
    }

    override fun mapFactory(): MapFactory = TestMapFactory()
}
