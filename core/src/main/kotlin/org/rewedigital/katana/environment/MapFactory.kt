package org.rewedigital.katana.environment

interface MapFactory {

    fun <K, V> create(initialCapacity: Int? = null): MutableMap<K, V>
}
