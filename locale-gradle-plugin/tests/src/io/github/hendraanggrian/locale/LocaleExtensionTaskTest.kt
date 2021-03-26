package io.github.hendraanggrian.locale

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

class LocaleExtensionTaskTest {

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
    fun noConfiguration() {
        buildFile.writeText(
            """
            plugins {
                java
                idea
                id("io.github.hendraanggrian.locale")
            }
            locale {
                "hi" {
                    en = "Hi"
                    id = "Hai"
                }
            }
            """.trimIndent()
        )
        runner.withArguments("localizeJvm").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":localizeJvm")!!.outcome)
            val enLines = testProjectDir.root.resolve("src")
                .resolve("main${File.separator}resources${File.separator}strings_en.properties")
                .readLines()
            assertTrue("hi=Hi" in enLines)
            val idLines = testProjectDir.root.resolve("src")
                .resolve("main${File.separator}resources${File.separator}strings_in.properties")
                .readLines()
            assertTrue("hi=Hai" in idLines)
        }
    }

    @Test
    fun configureAll() {
        buildFile.writeText(
            """
            plugins {
                java
                idea
                id("io.github.hendraanggrian.locale")
            }
            locale {
                resourceName.set("my_strings")
                defaultLocale.set(`java.util`.Locale.ENGLISH)
                outputDirectory.set(projectDir.resolve("res"))
                "hi" {
                    en = "Hi"
                    id = "Hai"
                }
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