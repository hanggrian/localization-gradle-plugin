package com.hendraanggrian.localization

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Gradle localization plugin with Kotlin DSL declaration. */
class LocalizationPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "localization"
        const val GROUP = PLUGIN_NAME
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create<LocalizationExtension>("localization", project)
        val localizeJvm by project.tasks.registering(LocalizeJvmTask::class) {
            group = GROUP
            description = "Write localization Java Properties file."
            resourceName.set(extension.resourceName)
            defaultLocale.set(extension.defaultLocale)
            outputDirectory.set(extension.outputDirectory)
        }
        val localizeAndroid by project.tasks.registering(LocalizeAndroidTask::class) {
            group = GROUP
            description = "Write localization Android XML files."
            resourceName.set(extension.resourceName)
            defaultLocale.set(extension.defaultLocale)
            outputDirectory.set(extension.outputDirectory)
        }
        val localizeAll by project.tasks.registering {
            group = GROUP
            description = "Write localization for Java and Android."
            dependsOn(localizeJvm, localizeAndroid)
        }
        project.afterEvaluate {
            localizeJvm { table.putAll(extension.table) }
            localizeAndroid { table.putAll(extension.table) }
        }
    }
}
