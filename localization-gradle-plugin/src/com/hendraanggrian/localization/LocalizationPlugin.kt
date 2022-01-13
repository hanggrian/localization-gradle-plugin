package com.hendraanggrian.localization

import com.hendraanggrian.localization.internal.AbstractLocalizeTask
import com.hendraanggrian.localization.internal.DefaultLocalization
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import kotlin.reflect.KClass

/** Gradle localization plugin with Kotlin DSL declaration. */
class LocalizationPlugin : Plugin<Project> {
    companion object {
        const val LOCALIZATION_GROUP = "localization"
        const val LOCALIZE_JVM_TASK_NAME = "localizeJvm"
        const val LOCALIZE_ANDROID_TASK_NAME = "localizeAndroid"
        const val LOCALIZE_ALL_TASK_NAME = "localizeAll"
    }

    override fun apply(project: Project) {
        val hasJavaPlugin = project.pluginManager.hasPlugin("java") || project.pluginManager.hasPlugin("java-library")

        val localization = project.extensions.create(
            Localization::class, "localization", DefaultLocalization::class, project
        )
        val localizeJvm = createLocalizeTask(project, LOCALIZE_JVM_TASK_NAME, LocalizeJvmTask::class, localization)
        val localizeAndroid = createLocalizeTask(
            project, LOCALIZE_ANDROID_TASK_NAME, LocalizeAndroidTask::class, localization
        )
        val localizeAll = project.tasks.register(LOCALIZE_ALL_TASK_NAME) {
            group = LOCALIZATION_GROUP
            description = "Creates localization files for both JVM and Android."
        }

        project.afterEvaluate {
            modifyLocalizeTask(localizeJvm, localization)
            modifyLocalizeTask(localizeAndroid, localization)
            if (hasJavaPlugin) {
                localizeJvm {
                    outputDirectory.convention(
                        project.extensions.getByName<SourceSetContainer>("sourceSets")["main"].resources.srcDirs.last()
                    )
                }
            }
            localizeAll { dependsOn(localizeJvm, localizeAndroid) }
        }
    }

    private fun <T : AbstractLocalizeTask> createLocalizeTask(
        project: Project,
        taskName: String,
        taskClass: KClass<T>,
        localization: Localization
    ): TaskProvider<T> = project.tasks.register(taskName, taskClass) {
        group = LOCALIZATION_GROUP
        description = "Creates localization files in platform-specific format."
        defaultLocale.convention(localization.defaultLocale)
        resourceName.convention(localization.resourceName)
    }

    private fun <T : AbstractLocalizeTask> modifyLocalizeTask(task: TaskProvider<T>, localization: Localization) {
        task {
            table.get().putAll(localization.table.get())
        }
    }
}
