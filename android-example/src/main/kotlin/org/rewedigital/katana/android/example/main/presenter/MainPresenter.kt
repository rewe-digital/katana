package org.rewedigital.katana.android.example.main.presenter

import android.util.Log
import kotlinx.coroutines.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.rewedigital.katana.android.example.main.interactor.MainInteractor
import org.rewedigital.katana.android.example.main.navigator.MainNavigator
import org.rewedigital.katana.android.example.main.view.MainView

interface MainPresenter {

    fun onCreate()

    fun onDestroy()

    fun onButtonClick(id: String)

    fun onRemoteButtonClick()

    fun onExitButtonClick()

    fun onDialogOkClick()
}

class MainPresenterImpl(
    private val view: MainView,
    private val interactor: MainInteractor,
    private val navigator: MainNavigator
) : MainPresenter,
    CoroutineScope {

    override val coroutineContext = Dispatchers.Main + Job()

    override fun onCreate() {
        interactor.fetchButtons().forEach { button ->
            view.addButton(button.id, button.label)
        }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
    }

    override fun onButtonClick(id: String) {
        interactor.findButtonById(id)?.let { button ->
            view.showToast(button.message)
        }
    }

    override fun onRemoteButtonClick() {
        val ref = asReference()
        launch {
            try {
                ref().view.showDialog()
                val posts = ref().interactor.fetchPosts()
                ref().view.setDialogResult(posts[0].body)
            } catch (e: Exception) {
                Log.e(TAG, "Could not load posts", e)
            }
        }
    }

    override fun onExitButtonClick() {
        navigator.exit()
    }

    override fun onDialogOkClick() {
        view.dismissDialog()
    }

    private companion object {
        private const val TAG = "MainPresenter"
    }
}
