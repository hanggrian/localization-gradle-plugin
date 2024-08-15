package com.hanggrian.localization

import com.hanggrian.localization.internal.DefaultLocalizationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.plugin.management.internal.autoapply.AutoAppliedDevelocityPlugin.GROUP

/**
 * Cross-platform localization generated in Gradle.
 *
 * @see <a href="https://github.com/hanggrian/localization-gradle-plugin">localization-gradle-plugin</a>
 */
public class LocalizationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val hasJavaPlugin =
            project.pluginManager.hasPlugin("java") ||
                project.pluginManager.hasPlugin("java-library")

        val localization =
            project.extensions.create(
                LocalizationExtension::class,
                "localization",
                DefaultLocalizationExtension::class,
                project.objects,
                project.logger,
            )
        val localizeJvm =
            project.createLocalizeTask<LocalizeJvmTask>(TASK_LOCALIZE_JVM, localization)
        val localizeAndroid =
            project.createLocalizeTask<LocalizeAndroidTask>(TASK_LOCALIZE_ANDROID, localization)
        project.tasks.register(TASK_LOCALIZE_ALL) { task ->
            task.group = GROUP
            task.description = "Creates localization files for both JVM and Android."
            task.dependsOn(localizeJvm, localizeAndroid)
        }

        project.afterEvaluate {
            if (!hasJavaPlugin) {
                return@afterEvaluate
            }
            localizeJvm {
                outputDirectory.convention(
                    project.extensions
                        .getByName<SourceSetContainer>("sourceSets")["main"]
                        .resources
                        .srcDirs
                        .lastOrNull(),
                )
            }
        }
    }

    private inline fun <reified T : AbstractLocalizeTask> Project.createLocalizeTask(
        taskName: String,
        localization: LocalizationExtension,
    ): TaskProvider<T> =
        tasks.register<T>(taskName) {
            group = GROUP
            description = "Creates localization files in platform-specific format."
            table.convention(localization.table)
            defaultLocale.convention(localization.defaultLocale)
            resourceName.convention(localization.resourceName)
        }

    public companion object {
        public const val GROUP: String = "localization"
        public const val TASK_LOCALIZE_JVM: String = "localizeJvm"
        public const val TASK_LOCALIZE_ANDROID: String = "localizeAndroid"
        public const val TASK_LOCALIZE_ALL: String = "localizeAll"
    }
}
