# AndroidX ViewModel extensions for Katana

**Attention**: Starting with Kotlin 1.3.30 (Katana 1.6.1), Kotlin introduced a compiler bug that currently breaks usage
of the inline functions of this artifact (issue #7). Until the Kotlin bug is fixed, please use Katana 1.6.0.

The extensions declared in this artifact simplify [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
injection with Katana.

Create a ViewModel binding inside a module:

```kotlin
data class MyViewModel(val someDependency: SomeDependency) : ViewModel()

Module {
    
    viewModel { MyViewModel(get()) }
}
```

Inject ViewModel in your `Activity` or `Fragment`:

```kotlin
class MyFragment : Fragment(),
                   KatanaTrait {
                   
    override val component = Component(...)
    
    private val viewModel by viewModel<MyViewModel, MyFragment>() 
}
```
