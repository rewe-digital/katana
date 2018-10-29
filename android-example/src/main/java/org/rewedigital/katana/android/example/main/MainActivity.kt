package org.rewedigital.katana.android.example.main

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.KatanaApp
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.createComponent
import org.rewedigital.katana.inject

class MainActivity : AppCompatActivity(), KatanaTrait {

    override val component = createComponent(
        modules = listOf(createMainModule(this)),
        dependsOn = listOf(KatanaApp.applicationComponent)
    )
    private val greeter: Greeter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            greeter.greet()
        }
    }
}
