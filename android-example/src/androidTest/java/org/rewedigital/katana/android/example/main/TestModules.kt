package org.rewedigital.katana.android.example.main

import androidx.test.espresso.idling.CountingIdlingResource
import org.rewedigital.katana.android.example.remote.*
import org.rewedigital.katana.createModule
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get

val testSuccessApiMockModule = createModule("testSuccessApiMockModule") {

    singleton<JsonPlaceholderApi> {
        JsonPlaceholderApiSuccessMock()
    }
}

val testErrorApiMockModule = createModule("testErrorApiMockModule") {

    singleton<JsonPlaceholderApi> {
        JsonPlaceholderApiErrorMock()
    }
}

const val TEST_REPO = "TEST_REPO"
const val TEST_IDLING_RESOURCE = "TEST_IDLING_RESOURCE"

fun createTestRepositoryModule(repositoryIdlingResource: CountingIdlingResource) =
    createModule("testRepositoryModule") {

        singleton(name = TEST_IDLING_RESOURCE) { repositoryIdlingResource }

        singleton<JsonPlaceholderRepository>(name = TEST_REPO) { JsonPlaceholderRepositoryImpl(get()) }

        singleton<JsonPlaceholderRepository> {
            TestJsonPlaceholderRepository(
                get(TEST_REPO),
                get(TEST_IDLING_RESOURCE)
            )
        }
    }
