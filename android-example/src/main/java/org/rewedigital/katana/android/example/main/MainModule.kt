package org.rewedigital.katana.android.example.main

import android.app.Activity
import android.content.Context
import org.rewedigital.katana.android.example.main.interactor.MainInteractor
import org.rewedigital.katana.android.example.main.interactor.MainInteractorImpl
import org.rewedigital.katana.android.example.main.mapper.ButtonMapper
import org.rewedigital.katana.android.example.main.mapper.ButtonMapperImpl
import org.rewedigital.katana.android.example.main.navigator.MainNavigator
import org.rewedigital.katana.android.example.main.navigator.MainNavigatorImpl
import org.rewedigital.katana.android.example.main.presenter.MainPresenter
import org.rewedigital.katana.android.example.main.presenter.MainPresenterImpl
import org.rewedigital.katana.android.example.main.repository.ButtonRepository
import org.rewedigital.katana.android.example.main.repository.ButtonRepositoryImpl
import org.rewedigital.katana.android.example.main.view.MainActivity
import org.rewedigital.katana.android.example.main.view.MainView
import org.rewedigital.katana.createModule

/**
 * This module demonstrates how the current [Context] can be injected safely.
 * In order for this to work without introducing leaks the module must be installed in a
 * [org.rewedigital.katana.Component] which is *only* referenced from the same Activity that
 * created the component.
 *
 * @see MainActivity
 * @see ButtonMapper
 */
fun createMainModule(activity: MainActivity) = createModule {

    bind<Context> { singleton { activity } }

    bind<Activity> { singleton { activity } }

    bind<ButtonMapper> { singleton { ButtonMapperImpl(get()) } }

    bind<ButtonRepository> { singleton { ButtonRepositoryImpl() } }

    bind<MainView> { singleton { activity } }

    bind<MainPresenter> { singleton { MainPresenterImpl(get(), get(), get()) } }

    bind<MainInteractor> { singleton { MainInteractorImpl(get(), get()) } }

    bind<MainNavigator> { singleton { MainNavigatorImpl(get()) } }
}
