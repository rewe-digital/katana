package org.rewedigital.katana.android.example.remote

import kotlinx.coroutines.Deferred
import org.rewedigital.katana.android.example.remote.model.Post
import retrofit2.http.GET

interface JsonPlaceholderApi {

    @GET("posts")
    fun posts(): Deferred<List<Post>>
}
