# AndroidX ViewModel Saved State extensions for Katana

The extensions declared in this artifact provide support for injection of [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
with [Saved State module](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate). Also see
[katana-androidx-viewmodel](../androidx-viewmodel).

Create a ViewModel binding inside a module:

```kotlin
data class MyViewModel(
    val state: SavedStateHandle,
    val someDependency: SomeDependency
) : ViewModel() {

    var someValue: String?
        get() = state.get("SOME_VALUE")
        set(value) {
            state.set("SOME_VALUE", value)
        }
}

Module {
    
    viewModelSavedState { state -> MyViewModel(state, get()) }
}
```

Inject ViewModel in your `Activity` or `Fragment`:

```kotlin
class MyFragment : Fragment(),
                   KatanaTrait {
                   
    override val component = Component(...)
    
    private val viewModel by viewModelSavedState<MyViewModel, MyFragment>() 
}
```
