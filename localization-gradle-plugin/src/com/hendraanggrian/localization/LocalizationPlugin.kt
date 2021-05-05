package com.hendraanggrian.localization

import com.hendraanggrian.localization.internal.DefaultLocalizationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Gradle localization plugin with Kotlin DSL declaration. */
class LocalizationPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "localization"
        const val GROUP_NAME = PLUGIN_NAME
    }

    private var hasJavaPlugin: Boolean = false
    private lateinit var extension: LocalizationExtension

    override fun apply(project: Project) {
        hasJavaPlugin = project.pluginManager.hasPlugin("java")
        extension = project.extensions.create(
            LocalizationExtension::class, "localization",
            DefaultLocalizationExtension::class, project
        )

        val localizeJvm by project.tasks.registering(LocalizeJvmTask::class) { setup("Java Properties") }
        val localizeAndroid by project.tasks.registering(LocalizeAndroidTask::class) { setup("Android XML") }
        val localizeAll by project.tasks.registering {
            group = GROUP_NAME
            description = "Write localization for Java and Android."
        }

        project.afterEvaluate {
            if (hasJavaPlugin) {
                val sourceSets = project.extensions.getByType<SourceSetContainer>()
                localizeJvm { outputDirectory.set(sourceSets["main"].resources.srcDirs.last()) }
            }
            val availableTasks = listOf(localizeJvm, localizeAndroid)
                .filter { it.get().outputDirectory.isPresent }
            when {
                availableTasks.isNotEmpty() -> localizeAll { dependsOn(availableTasks) }
                else -> localizeAll { doFirst { error("No localization configured") } }
            }
        }
    }

    private fun AbstractLocalizeTask.setup(target: String) {
        group = GROUP_NAME
        description = "Write localization $target file."
        table.set(extension.table)
        defaultLocale.set(extension.defaultLocale)
        resourceName.set(extension.resourceName)
    }
}
