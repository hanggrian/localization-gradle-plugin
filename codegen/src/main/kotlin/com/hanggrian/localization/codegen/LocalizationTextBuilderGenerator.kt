package com.hanggrian.localization.codegen

import com.hanggrian.kotlinpoet.CONST
import com.hanggrian.kotlinpoet.PRIVATE
import com.hanggrian.kotlinpoet.annotation
import com.hanggrian.kotlinpoet.buildFileSpec
import com.hanggrian.kotlinpoet.classNamed
import com.hanggrian.kotlinpoet.companionObjectType
import com.hanggrian.kotlinpoet.function
import com.hanggrian.kotlinpoet.getter
import com.hanggrian.kotlinpoet.interfaceType
import com.hanggrian.kotlinpoet.parameter
import com.hanggrian.kotlinpoet.property
import com.hanggrian.kotlinpoet.setter
import com.squareup.kotlinpoet.NOTHING
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess

object LocalizationTextBuilderGenerator {
    private const val PACKAGE_NAME = "com.hanggrian.localization"
    private const val CLASS_NAME = "LocalizationTextScope"

    @JvmStatic
    fun main(args: Array<String>) {
        println("Fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("Writing...")
        buildFileSpec(PACKAGE_NAME, CLASS_NAME) {
            indentSize = 4
            annotation(Suppress::class) {
                member("%S", "ktlint:rulebook:file-size-limitation")
            }
            interfaceType(CLASS_NAME) {
                kdoc.addStatement(
                    "Locales listed below are based on " +
                        "[umpirsky's locale-list](https://github.com/umpirsky/locale-list).",
                )
                kdoc.add("This is a generated class from `codegen` module.")

                superinterfaces[classNamed(PACKAGE_NAME, "Base$CLASS_NAME")] = null
                annotation(classNamed(PACKAGE_NAME, "LocalizationConfigurationDsl"))
                companionObjectType {
                    modifiers(PRIVATE)
                    property("NO_GETTER", String::class) {
                        initializer("%S", "Property does not have a getter.")
                        modifiers(CONST)
                    }
                    function("noGetter") {
                        returns = NOTHING
                        appendLine("return throw UnsupportedOperationException(NO_GETTER)")
                    }
                }
                locales.forEach { locale ->
                    val split = locale.split('_')
                    val language = split[0]
                    val country = split.getOrNull(1)
                    property(locale, String::class) {
                        kdoc.add("Set locale value with language `$language`")
                        if (country != null) kdoc.add(" and country `$country`")
                        kdoc.add(".")

                        isMutable = true
                        getter {
                            annotation(Deprecated::class) {
                                member("NO_GETTER")
                                member("level = DeprecationLevel.ERROR")
                            }
                            appendLine("return noGetter()")
                        }
                        setter {
                            parameter<String>("value")
                            when (country) {
                                null -> appendLine("return add(%S, value)", language)
                                else -> appendLine("return add(%S, %S, value)", language, country)
                            }
                        }
                    }
                }
            }
        }.writeTo(File("localization-gradle-plugin/src/main/kotlin"))

        println("Done!")
        exitProcess(0)
    }
}
