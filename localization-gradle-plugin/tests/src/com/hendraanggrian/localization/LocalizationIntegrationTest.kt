package com.hendraanggrian.localization

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalizationIntegrationTest {

    @Rule @JvmField val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "integration-test"
            """.trimIndent()
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner = GradleRunner.create()
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
                id("com.hendraanggrian.localization")
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
            """.trimIndent()
        )
        runner.withArguments("localizeJvm").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":localizeJvm")!!.outcome)
            val enLines = testProjectDir.root.resolve("res/strings.properties").readLines()
            assertTrue("hi=Hi" in enLines)
            val idLines = testProjectDir.root.resolve("res/strings_id.properties").readLines()
            assertTrue("hi=Hai" in idLines)
        }
    }
}