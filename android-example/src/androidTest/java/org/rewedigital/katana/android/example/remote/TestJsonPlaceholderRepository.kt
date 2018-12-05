package org.rewedigital.katana.android.example.remote

import androidx.test.espresso.idling.CountingIdlingResource
import org.rewedigital.katana.android.example.remote.model.Post

class TestJsonPlaceholderRepository(
    private val repository: JsonPlaceholderRepository,
    private val repositoryIdlingResource: CountingIdlingResource
) : JsonPlaceholderRepository {

    override suspend fun posts(): List<Post> {
        repositoryIdlingResource.increment()
        try {
            return repository.posts()
        } finally {
            repositoryIdlingResource.decrement()
        }
    }
}
