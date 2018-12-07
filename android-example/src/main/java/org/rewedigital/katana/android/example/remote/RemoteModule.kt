package org.rewedigital.katana.android.example.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.rewedigital.katana.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshiModule = createModule("moshiModule") {

    bind<Moshi> {
        singleton {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }
    }
}

val retrofitModule = createModule("retrofitModule") {

    bind<Retrofit.Builder> {
        factory {
            Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(get()))
        }
    }
}

val apiModule = createModule("apiModule") {

    bind<JsonPlaceholderApi> {
        singleton {
            get<Retrofit.Builder>()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()
                .create(JsonPlaceholderApi::class.java)
        }
    }
}

val repositoryModule = createModule("repositoryModule") {

    bind<JsonPlaceholderRepository> { singleton { JsonPlaceholderRepositoryImpl(get()) } }
}
