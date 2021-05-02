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
        const val GROUP_NAME = "localization"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<LocalizationExtension>("lokkal", project)

        val localizeJvm by project.tasks.registering(LocalizeJvmTask::class) {
            group = GROUP_NAME
            description = "Write localization Java Properties file."
            resourceName.set(ext.resourceName)
            defaultLocale.set(ext.defaultLocale)
            outputDirectory.set(ext.outputDirectory)
        }
        val localizeAndroid by project.tasks.registering(LocalizeAndroidTask::class) {
            group = GROUP_NAME
            description = "Write localization Android XML files."
            resourceName.set(ext.resourceName)
            defaultLocale.set(ext.defaultLocale)
            outputDirectory.set(ext.outputDirectory)
        }
        val localizeAll by project.tasks.registering {
            group = GROUP_NAME
            description = "Write localization for Java and Android."
            dependsOn(localizeJvm, localizeAndroid)
        }

        project.afterEvaluate {
            localizeJvm { table.putAll(ext.table) }
            localizeAndroid { table.putAll(ext.table) }
        }
    }
}
