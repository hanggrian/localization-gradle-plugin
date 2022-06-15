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

/**
 * Cross-platform localization generated in Gradle.
 *
 * @see <a href="https://github.com/hendraanggrian/localization-gradle-plugin">localization-gradle-plugin</a>
 */
class LocalizationPlugin : Plugin<Project> {
    companion object {
        const val GROUP = "localization"
        const val TASK_LOCALIZE_JVM = "localizeJvm"
        const val TASK_LOCALIZE_ANDROID = "localizeAndroid"
        const val TASK_LOCALIZE_ALL = "localizeAll"
    }

    override fun apply(project: Project) {
        val hasJavaPlugin = project.pluginManager.hasPlugin("java") || project.pluginManager.hasPlugin("java-library")

        val localization = project.extensions.create(
            Localization::class, "localization", DefaultLocalization::class, project
        )
        val localizeJvm = createLocalizeTask(project, TASK_LOCALIZE_JVM, LocalizeJvmTask::class, localization)
        val localizeAndroid = createLocalizeTask(
            project, TASK_LOCALIZE_ANDROID, LocalizeAndroidTask::class, localization
        )
        val localizeAll = project.tasks.register(TASK_LOCALIZE_ALL) {
            group = GROUP
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
        group = GROUP
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
