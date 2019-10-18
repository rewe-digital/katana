package org.rewedigital.katana.android.example.main

import org.rewedigital.katana.android.example.remote.ApiModule
import org.rewedigital.katana.android.example.remote.MoshiModule
import org.rewedigital.katana.android.example.remote.RepositoryModule
import org.rewedigital.katana.android.example.remote.RetrofitModule

object Modules {

    // Additional modules for main feature are placed here.
    // We can replace this list during UI tests with mock modules.
    var modules = listOf(
        MoshiModule,
        RetrofitModule,
        ApiModule,
        RepositoryModule
    )
}
