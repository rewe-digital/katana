package org.rewedigital.katana.android.example.main

import androidx.test.espresso.idling.CountingIdlingResource
import org.rewedigital.katana.android.example.remote.*
import org.rewedigital.katana.bind
import org.rewedigital.katana.createModule
import org.rewedigital.katana.get
import org.rewedigital.katana.singleton

val testSuccessApiMockModule = createModule("testSuccessApiMockModule") {

    bind<JsonPlaceholderApi> {
        singleton {
            JsonPlaceholderApiSuccessMock()
        }
    }
}

val testErrorApiMockModule = createModule("testErrorApiMockModule") {

    bind<JsonPlaceholderApi> {
        singleton {
            JsonPlaceholderApiErrorMock()
        }
    }
}

const val TEST_REPO = "TEST_REPO"
const val TEST_IDLING_RESOURCE = "TEST_IDLING_RESOURCE"

fun createTestRepositoryModule(repositoryIdlingResource: CountingIdlingResource) =
    createModule("testRepositoryModule") {

        bind<CountingIdlingResource>(TEST_IDLING_RESOURCE) { singleton { repositoryIdlingResource } }

        bind<JsonPlaceholderRepository>(TEST_REPO) { singleton { JsonPlaceholderRepositoryImpl(get()) } }

        bind<JsonPlaceholderRepository> {
            singleton {
                TestJsonPlaceholderRepository(
                    get(TEST_REPO),
                    get(TEST_IDLING_RESOURCE)
                )
            }
        }
    }
