package org.rewedigital.katana.android.example.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rewedigital.katana.android.example.remote.model.Post

interface JsonPlaceholderRepository {

    suspend fun posts(): List<Post>
}

class JsonPlaceholderRepositoryImpl(
    private val api: JsonPlaceholderApi
) : JsonPlaceholderRepository {

    override suspend fun posts() =
        withContext(Dispatchers.IO) {
            api.posts().await()
        }
}
