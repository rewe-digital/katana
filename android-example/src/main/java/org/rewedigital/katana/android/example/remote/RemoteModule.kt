package org.rewedigital.katana.android.example.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.rewedigital.katana.createModule
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshiModule = createModule("moshiModule") {

    singleton {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

val retrofitModule = createModule("retrofitModule") {

    factory {
        Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(get()))
    }
}

val apiModule = createModule("apiModule") {

    singleton {
        get<Retrofit.Builder>()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()
            .create(JsonPlaceholderApi::class.java)
    }
}

val repositoryModule = createModule("repositoryModule") {

    singleton<JsonPlaceholderRepository> { JsonPlaceholderRepositoryImpl(get()) }
}
