package org.rewedigital.katana.android.example.main.view

interface MainView {

    fun addButton(id: String, label: String)

    fun showToast(message: String)

    fun showDialog()

    fun setDialogResult(result: String)

    fun dismissDialog()
}
