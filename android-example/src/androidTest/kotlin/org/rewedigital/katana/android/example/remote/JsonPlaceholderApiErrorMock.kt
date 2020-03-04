package org.rewedigital.katana.android.example.remote

import kotlinx.coroutines.Deferred
import org.rewedigital.katana.android.example.remote.model.Post

class JsonPlaceholderApiErrorMock : JsonPlaceholderApi {

    override fun posts(): Deferred<List<Post>> {
        throw IllegalArgumentException()
    }
}
