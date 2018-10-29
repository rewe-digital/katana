package org.rewedigital.katana.android.example.main

import android.content.Context
import android.widget.Toast
import org.rewedigital.katana.android.example.R

class Greeter(private val context: Context) {

    fun greet() {
        Toast.makeText(context, R.string.greeting, Toast.LENGTH_LONG).show()
    }
}
