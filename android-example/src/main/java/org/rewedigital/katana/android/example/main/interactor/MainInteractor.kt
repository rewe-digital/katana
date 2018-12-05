package org.rewedigital.katana.android.example.main.interactor

import org.rewedigital.katana.android.example.main.mapper.ButtonMapper
import org.rewedigital.katana.android.example.main.model.ViewButton
import org.rewedigital.katana.android.example.main.repository.ButtonRepository
import org.rewedigital.katana.android.example.remote.JsonPlaceholderRepository
import org.rewedigital.katana.android.example.remote.model.Post

interface MainInteractor {

    fun fetchButtons(): List<ViewButton>

    fun findButtonById(id: String): ViewButton?

    suspend fun fetchPosts(): List<Post>
}

class MainInteractorImpl(
    private val buttonRepository: ButtonRepository,
    private val jsonPlaceholderRepository: JsonPlaceholderRepository,
    private val mapper: ButtonMapper
) : MainInteractor {

    override fun fetchButtons() = buttonRepository.fetch().map(mapper::map).reversed()

    override fun findButtonById(id: String) = buttonRepository.findById(id)?.let(mapper::map)

    override suspend fun fetchPosts() = jsonPlaceholderRepository.posts()
}
