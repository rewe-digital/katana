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

val MoshiModule = Module("MoshiModule") {

    singleton {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

val RetrofitModule = Module("RetrofitModule") {

    factory {
        Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(get()))
    }
}

val ApiModule = Module("ApiModule") {

    singleton {
        get<Retrofit.Builder>()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()
            .create(JsonPlaceholderApi::class.java)
    }
}

val RepositoryModule = Module("RepositoryModule") {

    singleton<JsonPlaceholderRepository> { JsonPlaceholderRepositoryImpl(get()) }
}
