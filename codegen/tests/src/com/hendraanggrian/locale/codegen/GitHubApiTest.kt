package com.hendraanggrian.locale.codegen

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class GitHubApiTest {

    @Test fun getLocales() {
        runBlocking { GitHubApi.getLocales() }.forEach { locale ->
            locale.split('_').forEachIndexed { index, s ->
                when (index) {
                    0 -> assertTrue(s.all { it.isLowerCase() })
                    1 -> assertTrue(s.all { it.isUpperCase() })
                    else -> fail()
                }
            }
        }
    }
}