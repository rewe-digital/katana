package org.rewedigital.katana.android.example.main.interactor

import org.rewedigital.katana.android.example.main.mapper.ButtonMapper
import org.rewedigital.katana.android.example.main.model.ViewButton
import org.rewedigital.katana.android.example.main.repository.ButtonRepository

interface MainInteractor {

    fun fetchButtons(): List<ViewButton>

    fun findButtonById(id: String): ViewButton?
}

class MainInteractorImpl(
    private val repository: ButtonRepository,
    private val mapper: ButtonMapper
) : MainInteractor {

    override fun fetchButtons() = repository.fetch().map(mapper::map).reversed()

    override fun findButtonById(id: String) = repository.findById(id)?.let(mapper::map)
}
