package com.hendraanggrian.localization.codegen

import com.hendraanggrian.kotlinpoet.CONST
import com.hendraanggrian.kotlinpoet.PRIVATE
import com.hendraanggrian.kotlinpoet.buildFileSpec
import com.hendraanggrian.kotlinpoet.classNameOf
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.NOTHING
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess

object LocalizationTextBuilderGenerator {
    private const val PACKAGE_NAME = "com.hendraanggrian.localization"
    private const val CLASS_NAME = "LocalizationTextScope"

    @JvmStatic
    fun main(args: Array<String>) {
        println("Fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("Writing...")
        buildFileSpec(PACKAGE_NAME, CLASS_NAME) {
            indentSize = 4
            addImport("java.util", "Locale")
            types.addInterface(CLASS_NAME) {
                kdoc {
                    appendLine("Locales listed below are based on [umpirsky's locale-list](https://github.com/umpirsky/locale-list).")
                    appendLine()
                    appendLine("This is a generated class from `codegen` module.")
                }
                superinterfaces[classNameOf(PACKAGE_NAME, "Base$CLASS_NAME")] = null
                annotations.add(ClassName(PACKAGE_NAME, "LocalizationConfigurationDsl"))
                types.addCompanionObject {
                    addModifiers(PRIVATE)
                    properties.add<String>("NO_GETTER") {
                        initializer("%S", "Property does not have a getter.")
                        addModifiers(CONST)
                    }
                    functions.add("noGetter") {
                        returns = NOTHING
                        appendLine("return throw UnsupportedOperationException(NO_GETTER)")
                    }
                }
                locales.forEach { locale ->
                    val split = locale.split('_')
                    val language = split[0]
                    val country = split.getOrNull(1)
                    properties.add<String>(locale) {
                        isMutable = true
                        kdoc {
                            append("Set locale value with language `$language`")
                            if (country != null) append(" and country `$country`")
                            append(".")
                        }
                        getter {
                            annotations.add<Deprecated> {
                                addMember("NO_GETTER")
                                addMember("level = DeprecationLevel.ERROR")
                            }
                            appendLine("return noGetter()")
                        }
                        setter {
                            parameters.add<String>("value")
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
