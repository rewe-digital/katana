package org.rewedigital.katana.android.environment

import androidx.collection.ArrayMap
import org.rewedigital.katana.environment.EnvironmentContext
import org.rewedigital.katana.environment.MapFactory

object AndroidEnvironmentContext : EnvironmentContext {

    private class AndroidMapFactory : MapFactory {

        override fun <K, V> create(initialCapacity: Int?): MutableMap<K, V> =
            if (initialCapacity == null) {
                ArrayMap()
            } else {
                ArrayMap(initialCapacity)
            }
    }

    override fun mapFactory(): MapFactory = AndroidMapFactory()
}
