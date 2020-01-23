# Fragment constructor injection with FragmentFactory

This artifact provides additional support for `androidx.fragment` enabling constructor injection in Fragments with
`KatanaFragmentFactory`.

```kotlin
// In a module: 

val module = Module {

    singleton {
        KatanaFragmentFactory(component)
            .handlesFragment<FirstFragment>()
            .handlesFragment<SecondFragment>(name = "SecondFragment")
    }

    factory { FirstFragment(get()) }

    factory(name = "SecondFragment") { SecondFragment(get(), get()) }
}

// ... then in an Activity:

class MyActivity : AppCompatActivity() {

    private val fragmentFactory by applicationComponent.inject<KatanaFragmentFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be set **before** super call for Fragment instantiation after orientation change
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
    
        // ...
    }
}
```
