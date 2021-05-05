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

class LocalizationFunctionalTest {
    @Rule @JvmField val testProjectDir = TemporaryFolder()
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle.kts")
        settingsFile.writeText(
            """
            rootProject.name = "functional-test"
            """.trimIndent()
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    @Test
    fun minimalConfiguration() {
        buildFile.writeText(
            """
            plugins {
                id("com.hendraanggrian.localization")
            }
            localization {
                "hi" {
                    en = "Hi"
                    id = "Hai"
                }
            }
            tasks.localizeJvm {
                outputDirectory.set(projectDir.resolve("src/main/resources"))
            }
            """.trimIndent()
        )
        runner.withArguments("localizeJvm").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":localizeJvm")!!.outcome)
            val enLines = testProjectDir.root.resolve("src")
                .resolve("main/resources/strings_en.properties")
                .readLines()
            assertTrue("hi=Hi" in enLines)
            val idLines = testProjectDir.root.resolve("src")
                .resolve("main/resources/strings_in.properties")
                .readLines()
            assertTrue("hi=Hai" in idLines)
        }
    }

    // @Test
    fun configureSome() {
        buildFile.writeText(
            """
            plugins {
                java
                id("com.hendraanggrian.localization")
            }
            localization {
                resourceName.set("my_strings")
                defaultLocale.set(`java.util`.Locale.ENGLISH)
                "hi" {
                    en = "Hi"
                    id = "Hai"
                }
            }
            localizeJvm {
                outputDirectory.set(projectDir.resolve("res"))
            }
            """.trimIndent()
        )
        runner.withArguments("localizeJvm").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":localizeJvm")!!.outcome)
            val enLines = testProjectDir.root.resolve("res")
                .resolve("my_strings.properties")
                .readLines()
            assertTrue("hi=Hi" in enLines)
            val idLines = testProjectDir.root.resolve("res")
                .resolve("my_strings_in.properties")
                .readLines()
            assertTrue("hi=Hai" in idLines)
        }
    }
}