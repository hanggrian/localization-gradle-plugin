package com.hanggrian.localization

import com.hanggrian.localization.LocalizationPlugin.Companion.TASK_LOCALIZE_JVM
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalizationIntegrationTest {
    @Rule @JvmField
    val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "integration-test"
            """.trimIndent(),
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner =
            GradleRunner
                .create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.root)
                .withTestKitDir(testProjectDir.newFolder())
    }

    @Test
    fun withJavaPlugin() {
        buildFile.writeText(
            """
            plugins {
                java
                id("com.hanggrian.localization")
            }
            sourceSets.main {
                resources.srcDir("res")
            }
            localization {
                "hi" {
                    en = "Hi"
                    id = "Hai"
                }
            }
            """.trimIndent(),
        )
        assertEquals(
            SUCCESS,
            runner.withArguments(TASK_LOCALIZE_JVM).build().task(":$TASK_LOCALIZE_JVM")!!.outcome,
        )
        testProjectDir.root.resolve("res/strings.properties").readLines().let {
            assertTrue("hi=Hi" in it)
        }
        testProjectDir.root.resolve("res/strings_in.properties").readLines().let {
            assertTrue("hi=Hai" in it)
        }
    }
}
