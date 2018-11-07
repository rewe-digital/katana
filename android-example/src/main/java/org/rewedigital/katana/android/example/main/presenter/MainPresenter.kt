package org.rewedigital.katana.android.example.main.presenter

import org.rewedigital.katana.android.example.main.interactor.MainInteractor
import org.rewedigital.katana.android.example.main.navigator.MainNavigator
import org.rewedigital.katana.android.example.main.view.MainView

interface MainPresenter {

    fun onCreate()

    fun onDestroy()

    fun onButtonClick(id: String)

    fun onExitButtonClick()
}

class MainPresenterImpl(
    private val view: MainView,
    private val interactor: MainInteractor,
    private val navigator: MainNavigator
) : MainPresenter {

    override fun onCreate() {
        interactor.fetchButtons().forEach { button ->
            view.addButton(button.id, button.label)
        }
    }

    override fun onDestroy() {
    }

    override fun onButtonClick(id: String) {
        interactor.findButtonById(id)?.let { button ->
            view.showToast(button.message)
        }
    }

    override fun onExitButtonClick() {
        navigator.exit()
    }
}
