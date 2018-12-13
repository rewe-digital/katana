package org.rewedigital.katana.environment

object DefaultEnvironmentContext : EnvironmentContext {

    override fun <K, V> mapFactory() = { HashMap<K, V>() }
}
