package org.rewedigital.katana.android.example.main

import org.rewedigital.katana.android.example.remote.apiModule
import org.rewedigital.katana.android.example.remote.moshiModule
import org.rewedigital.katana.android.example.remote.repositoryModule
import org.rewedigital.katana.android.example.remote.retrofitModule

object Modules {

    // Additional modules for main feature are placed here.
    // We can replace this list during UI tests with mock modules.
    var modules = listOf(
        moshiModule,
        retrofitModule,
        apiModule,
        repositoryModule
    )
}
