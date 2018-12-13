package org.rewedigital.katana.android.environment

import androidx.collection.ArrayMap
import org.rewedigital.katana.environment.EnvironmentContext

object AndroidEnvironmentContext : EnvironmentContext {

    override fun <K, V> mapFactory() = { ArrayMap<K, V>() }
}
