package org.rewedigital.katana.androidx.viewmodel.internal

import androidx.lifecycle.ViewModel

/**
 * Returns name for [ViewModel] dependency binding.
 *
 * The binding under the returned name should only be used by these ViewModel extensions and not accessed
 * outside of this scope via `get()` or `inject()` for instance.
 */
fun <VM : ViewModel> viewModelName(modelClass: Class<VM>, key: String?) =
    "ViewModel\$\$${modelClass.name}\$\$${key ?: "DEFAULT_KEY"}"
