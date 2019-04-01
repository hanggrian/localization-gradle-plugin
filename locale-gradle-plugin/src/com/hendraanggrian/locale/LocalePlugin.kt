package com.hendraanggrian.locale

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate // ktlint-disable

class LocalePlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "locale"
    }

    @Suppress("UNUSED_VARIABLE")
    override fun apply(project: Project) {
        val ext = project.extensions.create<LocaleExtension>(GROUP_NAME)
        project.tasks {
            val writeResourceBundles by registering(WriteResourceBundlesTask::class) {
                group = GROUP_NAME
                setTable(ext.table)
                ext.resourceName?.let { resourceName = it }
                defaultLocale = ext.defaultLocale
            }
            val writeAndroidResources by registering(WriteAndroidResourcesTask::class) {
                group = GROUP_NAME
                setTable(ext.table)
                ext.resourceName?.let { resourceName = it }
                defaultLocale = ext.defaultLocale
            }
        }
    }
}