package com.hendraanggrian.locale

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Gradle localization plugin with Kotlin DSL declaration. */
class LocalePlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "localization"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<LocaleExtension>("locale")
        project.tasks {
            val localizeJava by registering(LocalizeJavaTask::class) {
                group = GROUP_NAME
                description = "Write localization Java Properties file."
                setTable(ext.javaTable)
                if (outputDir == null) {
                    outputDir = project.projectDir.resolve("src/main/resources")
                }
                if (resourceName == null) {
                    resourceName = ext.resourceName
                }
                if (defaultLocale == null && ext.defaultLocale != null) {
                    defaultLocale = ext.defaultLocale
                }
            }
            val localizeAndroid by registering(LocalizeAndroidTask::class) {
                group = GROUP_NAME
                description = "Write localization Android XML files."
                setTable(ext.androidTable)
                if (outputDir == null) {
                    outputDir = project.projectDir.resolve("src/main/resources")
                }
                if (resourceName == null) {
                    resourceName = ext.resourceName
                }
                if (defaultLocale == null && ext.defaultLocale != null) {
                    defaultLocale = ext.defaultLocale
                }
            }
            @Suppress("UNUSED_VARIABLE")
            val localizeAll by registering {
                group = GROUP_NAME
                description = "Write localization for Java and Android."
                dependsOn(
                    localizeJava.get(),
                    localizeAndroid.get()
                )
            }
        }
    }
}
