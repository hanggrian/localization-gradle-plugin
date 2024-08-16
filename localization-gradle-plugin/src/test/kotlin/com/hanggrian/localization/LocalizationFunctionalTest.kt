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

class LocalizationFunctionalTest {
    @Rule @JvmField
    val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "functional-test"
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
    fun minimalConfiguration() {
        buildFile.writeText(
            """
            plugins {
                id("com.hanggrian.localization")
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
        testProjectDir.root.resolve("src/main/resources/strings.properties").readLines().let {
            assertTrue("hi=Hi" in it)
        }
        testProjectDir.root.resolve("src/main/resources/strings_id.properties").readLines().let {
            assertTrue("hi=Hai" in it)
        }
    }
}
