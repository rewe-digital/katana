package org.rewedigital.katana.android.example.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.factory
import org.rewedigital.katana.dsl.singleton
import org.rewedigital.katana.dsl.get
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshiModule = Module("moshiModule") {

    singleton {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

val retrofitModule = Module("retrofitModule") {

    factory {
        Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(get()))
    }
}

val apiModule = Module("apiModule") {

    singleton {
        get<Retrofit.Builder>()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()
            .create(JsonPlaceholderApi::class.java)
    }
}

val repositoryModule = Module("repositoryModule") {

    singleton<JsonPlaceholderRepository> { JsonPlaceholderRepositoryImpl(get()) }
}
