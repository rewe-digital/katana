package org.rewedigital.katana.comparison

class MySingleton

interface MyDependency {

    fun execute()
}

@Suppress("unused")
class MyDependencyImpl(private val mySingleton: MySingleton) :
    MyDependency {

    override fun execute() {
    }
}

@Suppress("unused")
class MyDependency2(private val mySingleton: MySingleton) :
    MyDependency {

    override fun execute() {
    }
}
