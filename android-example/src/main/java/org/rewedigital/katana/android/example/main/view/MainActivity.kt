package org.rewedigital.katana.android.example.main.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.KatanaApp
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.main.Modules
import org.rewedigital.katana.android.example.main.createMainModule
import org.rewedigital.katana.android.example.main.presenter.MainPresenter
import org.rewedigital.katana.android.modules.createActivityModule
import org.rewedigital.katana.createComponent
import org.rewedigital.katana.inject

class MainActivity : AppCompatActivity(),
                     KatanaTrait,
                     MainView {

    // Component is created and referenced only from this Activity
    override val component = createComponent(
        modules = listOf(createActivityModule(this), createMainModule(this)) + Modules.modules,
        dependsOn = listOf(KatanaApp.applicationComponent)
    )
    private val presenter: MainPresenter by inject()

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        remoteButton.setOnClickListener { presenter.onRemoteButtonClick() }
        exitButton.setOnClickListener { presenter.onExitButtonClick() }

        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        dialog?.dismiss()
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

    override fun showDialog() {
        dialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_remote)
            .setPositiveButton(android.R.string.ok) { _, _ -> presenter.onDialogOkClick() }
            .setOnDismissListener { dialog = null }
            .show()
    }

    override fun setDialogResult(result: String) {
        dialog?.let { dlg ->
            dlg.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
            dlg.findViewById<TextView>(R.id.textView)?.let { textView ->
                textView.text = result
                textView.visibility = View.VISIBLE
            }
        }
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }
}
