package org.rewedigital.katana.android.example.main.repository

import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.main.model.Button

interface ButtonRepository {

    fun fetch(): List<Button>

    fun findById(id: String): Button?
}

class ButtonRepositoryImpl : ButtonRepository {

    override fun fetch() = BUTTONS

    override fun findById(id: String) = BUTTONS.find { button -> button.id == id }

    private companion object {

        private val BUTTONS = listOf(
            Button(
                id = "cd6a5c60-9ac6-401d-b92e-29a765fb56bf",
                labelResId = R.string.main_button1_label,
                messageResId = R.string.main_button1_message
            ),
            Button(
                id = "bae39808-9735-418a-9684-5f028f0dea91",
                labelResId = R.string.main_button2_label,
                messageResId = R.string.main_button2_message
            )
        )
    }
}
