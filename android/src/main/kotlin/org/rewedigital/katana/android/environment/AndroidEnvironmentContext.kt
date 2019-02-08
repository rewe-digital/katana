package org.rewedigital.katana.android.environment

import androidx.collection.ArrayMap
import org.rewedigital.katana.android.environment.AndroidEnvironmentContext.Profile.MEMORY
import org.rewedigital.katana.android.environment.AndroidEnvironmentContext.Profile.SPEED
import org.rewedigital.katana.environment.EnvironmentContext
import org.rewedigital.katana.environment.MapFactory

class AndroidEnvironmentContext(private val profile: Profile = MEMORY) : EnvironmentContext {

    enum class Profile {
        /**
         * Optimized on low memory usage at the cost of speed.
         */
        MEMORY,

        /**
         * Optimized on speed at the cost of memory usage.
         */
        SPEED
    }

    private class AndroidMemoryProfileMapFactory : MapFactory {

        override fun <K, V> create(initialCapacity: Int?): MutableMap<K, V> =
            if (initialCapacity == null) {
                ArrayMap()
            } else {
                ArrayMap(initialCapacity)
            }
    }

    private class AndroidSpeedProfileMapFactory : MapFactory {

        override fun <K, V> create(initialCapacity: Int?): MutableMap<K, V> =
            if (initialCapacity == null) {
                HashMap()
            } else {
                HashMap(initialCapacity)
            }
    }

    override fun mapFactory(): MapFactory =
        when (profile) {
            MEMORY -> AndroidMemoryProfileMapFactory()
            SPEED -> AndroidSpeedProfileMapFactory()
        }
}
