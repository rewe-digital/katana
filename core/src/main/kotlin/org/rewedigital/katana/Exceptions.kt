package org.rewedigital.katana

open class KatanaException(message: String, throwable: Throwable? = null) : RuntimeException(message, throwable)

open class ComponentException(message: String) : KatanaException(message)

class ComponentNotInitializedException(message: String) : ComponentException(message)

class InjectionException(message: String) : KatanaException(message)

class OverrideException(message: String) : KatanaException(message) {
    constructor(override: String, existing: String) : this("$override would override $existing")
}

class InstanceCreationException(message: String, cause: Throwable) : KatanaException(message, cause)
