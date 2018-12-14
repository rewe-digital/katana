package org.rewedigital.katana.environment

object DefaultEnvironmentContext : EnvironmentContext {

    private class DefaultMapFactory : MapFactory {

        override fun <K, V> create(initialCapacity: Int?): MutableMap<K, V> =
            if (initialCapacity == null) {
                HashMap()
            } else {
                HashMap(initialCapacity)
            }
    }

    override fun mapFactory(): MapFactory = DefaultMapFactory()
}
