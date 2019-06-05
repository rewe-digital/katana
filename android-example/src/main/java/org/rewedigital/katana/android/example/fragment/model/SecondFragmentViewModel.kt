package org.rewedigital.katana.android.example.fragment.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

data class SecondFragmentViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    var message: String?
        get() = state.get(KEY_MESSAGE)
        set(value) {
            state.set(KEY_MESSAGE, value)
        }

    private companion object {
        private const val KEY_MESSAGE = "MESSAGE"
    }
}
