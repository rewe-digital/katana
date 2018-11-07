package org.rewedigital.katana.android.example.main.model

import androidx.annotation.StringRes

data class Button(
    val id: String,
    @StringRes val labelResId: Int,
    @StringRes val messageResId: Int
)
