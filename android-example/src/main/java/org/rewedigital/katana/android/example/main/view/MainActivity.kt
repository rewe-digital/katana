package org.rewedigital.katana.android.example.main.view

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.KatanaApp
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.main.createMainModule
import org.rewedigital.katana.android.example.main.presenter.MainPresenter
import org.rewedigital.katana.createComponent
import org.rewedigital.katana.inject

class MainActivity : AppCompatActivity(), KatanaTrait, MainView {

    // Component is created and referenced only from this Activity
    override val component = createComponent(
        modules = listOf(createMainModule(this)),
        dependsOn = listOf(KatanaApp.applicationComponent)
    )
    private val presenter: MainPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.exitButton).setOnClickListener { presenter.onExitButtonClick() }

        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun addButton(id: String, label: String) {
        findViewById<ViewGroup>(R.id.rootView).let { root ->
            val button = Button(this).apply {
                text = label
                setOnClickListener {
                    presenter.onButtonClick(id)
                }
            }
            root.addView(button, 0)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
