package io.github.hendraanggrian.locale

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
        val ext = project.extensions.create<LocaleExtension>("locale", project.projectDir, project.logger)
        ext.outputDir = project.projectDir.resolve("src/main/resources")

        val localizeJava by project.tasks.registering(LocalizeJavaTask::class) {
            group = GROUP_NAME
            description = "Write localization Java Properties file."
            outputDir = ext.outputDir
            resourceName = ext.resourceName
            defaultLocale = ext.defaultLocale
        }
        val localizeAndroid by project.tasks.registering(LocalizeAndroidTask::class) {
            group = GROUP_NAME
            description = "Write localization Android XML files."
            outputDir = ext.outputDir
            resourceName = ext.resourceName
            defaultLocale = ext.defaultLocale
        }
        project.tasks.register("localizeAll") {
            group = GROUP_NAME
            description = "Write localization for Java and Android."
            dependsOn(
                localizeJava.get(),
                localizeAndroid.get()
            )
        }

        project.afterEvaluate {
            localizeJava {
                table.putAll(ext.table)
            }
            localizeAndroid {
                table.putAll(ext.table)
            }
        }
    }
}
