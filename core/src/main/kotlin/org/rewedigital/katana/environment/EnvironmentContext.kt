package org.rewedigital.katana.environment

interface EnvironmentContext {

    fun <K, V> mapFactory(): () -> MutableMap<K, V>
}
