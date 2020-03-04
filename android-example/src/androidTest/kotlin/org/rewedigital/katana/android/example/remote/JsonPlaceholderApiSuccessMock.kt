package org.rewedigital.katana.android.example.remote

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.rewedigital.katana.android.example.remote.model.Post

class JsonPlaceholderApiSuccessMock : JsonPlaceholderApi {

    override fun posts(): Deferred<List<Post>> =
        CompletableDeferred(
            listOf(
                Post(
                    userId = 1,
                    id = 1,
                    title = "Lorem ipsum",
                    body = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam"
                )
            )
        )
}
