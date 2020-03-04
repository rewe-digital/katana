package org.rewedigital.katana.android.example.main.mapper

import android.content.Context
import org.rewedigital.katana.android.example.main.model.Button
import org.rewedigital.katana.android.example.main.model.ViewButton

interface ButtonMapper {

    fun map(button: Button): ViewButton
}

class ButtonMapperImpl(private val context: Context) : ButtonMapper {

    override fun map(button: Button) =
        ViewButton(
            id = button.id,
            label = context.getString(button.labelResId),
            message = context.getString(button.messageResId)
        )
}
